package ai;

import ai.tools.AquariumTools;
import com.google.gson.Gson;
import com.google.genai.Client;
import com.google.genai.types.FunctionDeclaration;
import com.google.genai.types.Schema;
import com.google.genai.types.Type;
import com.google.genai.types.Tool;
import com.google.genai.types.GenerateContentConfig;
import com.google.genai.types.GenerateContentResponse;
import com.google.genai.types.FunctionCall;
import com.google.genai.types.Part;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableList;
import com.google.genai.types.Tool;
import com.google.genai.types.FunctionDeclaration;
import com.google.genai.Client;
import com.google.genai.types.Tool;
import com.google.genai.types.FunctionDeclaration;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Lớp Agent với Google GenAI SDK mới (com.google.genai).
 * Hỗ trợ Function Calling cho tư vấn cá cảnh, sản phẩm, bệnh và thức ăn.
 */
public class Agent {
    private final Client client;
    private final String modelName;
    private final AquariumTools aquariumTools;
    private final Gson gson = new Gson();

    // Các khai báo FunctionDeclaration lưu thành trường để tái sử dụng (tùy trường hợp)
    private final List<FunctionDeclaration> functionDeclarations;

    public Agent(String apiKey, String modelName) throws IOException {
        this.modelName = modelName;
        this.aquariumTools = new AquariumTools();
        this.client = Client.builder()
                .apiKey(apiKey)
                .build();
        // Khởi tạo FunctionDeclaration cho các function gọi được
        this.functionDeclarations = ImmutableList.of(
                FunctionDeclaration.builder()
                        .name("fishRecommendation")
                        .description("Tư vấn chọn cá cảnh phù hợp dựa trên kinh nghiệm người chơi (beginner, intermediate, advanced).")
                        .parameters(
                                Schema.builder()
                                        .type(Type.Known.OBJECT)
                                        .properties(ImmutableMap.of(
                                                "experience",
                                                Schema.builder().type(Type.Known.STRING)
                                                        .description("Kinh nghiệm của người chơi (beginner, intermediate, advanced).")
                                                        .build()
                                        ))
                                        .required(ImmutableList.of("experience"))
                                        .build()
                        )
                        .build(),
                FunctionDeclaration.builder()
                        .name("productInquiry")
                        .description("Tra cứu một sản phẩm cụ thể trong database. Dùng khi người dùng hỏi về một sản phẩm có tên cụ thể.")
                        .parameters(
                                Schema.builder()
                                        .type(Type.Known.OBJECT)
                                        .properties(ImmutableMap.of(
                                                "productName",
                                                Schema.builder().type(Type.Known.STRING)
                                                        .description("Tên sản phẩm cần tìm.")
                                                        .build()
                                        ))
                                        .required(ImmutableList.of("productName"))
                                        .build()
                        )
                        .build(),
                FunctionDeclaration.builder()
                        .name("feedingAdvice")
                        .description("Tư vấn các loại thức ăn phù hợp cho một loại cá cụ thể.")
                        .parameters(
                                Schema.builder()
                                        .type(Type.Known.OBJECT)
                                        .properties(ImmutableMap.of(
                                                "fishType",
                                                Schema.builder().type(Type.Known.STRING)
                                                        .description("Tên loại cá cần tư vấn.")
                                                        .build()
                                        ))
                                        .required(ImmutableList.of("fishType"))
                                        .build()
                        )
                        .build(),
                FunctionDeclaration.builder()
                        .name("diseaseDiagnosis")
                        .description("Chẩn đoán bệnh dựa trên triệu chứng và gợi ý sản phẩm điều trị.")
                        .parameters(
                                Schema.builder()
                                        .type(Type.Known.OBJECT)
                                        .properties(ImmutableMap.of(
                                                "symptoms",
                                                Schema.builder().type(Type.Known.STRING)
                                                        .description("Mô tả triệu chứng của cá.")
                                                        .build()
                                        ))
                                        .required(ImmutableList.of("symptoms"))
                                        .build()
                        )
                        .build()
        );
    }

    public String chat(String userMessage) throws IOException {
        // Tạo Tool từ các hàm đã định nghĩa
        Tool tool = Tool.builder()
    .functions(functionDeclarations) // functionDeclarations là List<FunctionDeclaration>
    .build();

        // Cấu hình để gọi model với Function Calling
        GenerateContentConfig config = GenerateContentConfig.builder()
                .tools(ImmutableList.of(tool))
                .build();
        // Gửi message tới model
        GenerateContentResponse response = client.models.generateContent(
                modelName,
                userMessage,
                config
        );
        // Xử lý function call nếu có
        List<FunctionCall> functionCalls = response.functionCalls();
        if (functionCalls != null && !functionCalls.isEmpty()) {
            FunctionCall functionCall = functionCalls.get(0); // chỉ xử lý function call đầu tiên (nếu nhiều)
            Map<String, Object> result = executeFunction(functionCall);
            // Gửi kết quả function call trở lại cho model
            GenerateContentResponse finalResponse = client.models.generateContent(
                    modelName,
                    Part.fromFunctionResponse(functionCall.name(), result),
                    null
            );
            return getText(finalResponse);
        }
        return getText(response);
    }

    private Map<String, Object> executeFunction(FunctionCall functionCall) {
        String functionName = functionCall.name();
        Map<String, Object> args = functionCall.args();
        if (args == null) args = new HashMap<>();
        switch (functionName) {
            case "fishRecommendation":
                return aquariumTools.fishRecommendation((String) args.get("experience"));
            case "productInquiry":
                return aquariumTools.productInquiry((String) args.get("productName"));
            case "feedingAdvice":
                return aquariumTools.feedingAdvice((String) args.get("fishType"));
            case "diseaseDiagnosis":
                return aquariumTools.diseaseDiagnosis((String) args.get("symptoms"));
            default:
                return Map.of("error", "Unknown function: " + functionName);
        }
    }

    private String getText(GenerateContentResponse response) {
        if (response == null || response.text() == null || response.text().trim().isEmpty()) {
            return "Xin lỗi, tôi không thể xử lý yêu cầu này ngay bây giờ.";
        }
        return response.text();
    }
}
