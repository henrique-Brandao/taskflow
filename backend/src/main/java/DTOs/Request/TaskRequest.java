package DTOs.Request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record TaskRequest(
        @NotBlank(message = "O titulo da tarefa é obrigatório")
        @Size(max = 120, message = "O titulo deve ter no máximo 120 caracteres")
        String title,
        String description
) {
}
