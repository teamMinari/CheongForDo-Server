package Minari.cheongForDo.domain.member.presentation.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberRegisterDTO {
    @NotBlank
    private String id;

    @NotBlank
    private String password;

    @NotBlank
    private String name;

    @NotBlank
    private String email;
}

