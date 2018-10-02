package de.adorsys.psd2.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Objects;

/**
 * The OAuth2 token cannot be matched by the ASPSP relative to the TPP.
 */
@ApiModel(description = "The OAuth2 token cannot be matched by the ASPSP relative to the TPP.")
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2018-10-02T14:31:50.251+02:00[Europe/Berlin]")
public class TppMessageGENERICTOKENUNKNOWN401 {
    @JsonProperty("category")
    private TppMessageCategory category = null;
    @JsonProperty("code")
    private CodeEnum code = null;
    @JsonProperty("path")
    private String path = null;
    @JsonProperty("text")
    private String text = null;

    public TppMessageGENERICTOKENUNKNOWN401 category(TppMessageCategory category) {
        this.category = category;
        return this;
    }

    /**
     * Get category
     *
     * @return category
     **/
    @ApiModelProperty(required = true)
    @NotNull
    @Valid
    public TppMessageCategory getCategory() {
        return category;
    }

    public void setCategory(TppMessageCategory category) {
        this.category = category;
    }

    public TppMessageGENERICTOKENUNKNOWN401 code(CodeEnum code) {
        this.code = code;
        return this;
    }

    /**
     * Get code
     *
     * @return code
     **/
    @ApiModelProperty(required = true)
    @NotNull
    public CodeEnum getCode() {
        return code;
    }

    public void setCode(CodeEnum code) {
        this.code = code;
    }

    public TppMessageGENERICTOKENUNKNOWN401 path(String path) {
        this.path = path;
        return this;
    }

    /**
     * Get path
     *
     * @return path
     **/
    @ApiModelProperty
    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public TppMessageGENERICTOKENUNKNOWN401 text(String text) {
        this.text = text;
        return this;
    }

    /**
     * Get text
     *
     * @return text
     **/
    @ApiModelProperty
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TppMessageGENERICTOKENUNKNOWN401 tppMessageGENERICTOKENUNKNOWN401 = (TppMessageGENERICTOKENUNKNOWN401) o;
        return Objects.equals(this.category, tppMessageGENERICTOKENUNKNOWN401.category) && Objects.equals(this.code, tppMessageGENERICTOKENUNKNOWN401.code) && Objects.equals(this.path, tppMessageGENERICTOKENUNKNOWN401.path) && Objects.equals(this.text, tppMessageGENERICTOKENUNKNOWN401.text);
    }

    @Override
    public int hashCode() {
        return Objects.hash(category, code, path, text);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class TppMessageGENERICTOKENUNKNOWN401 {\n");

        sb.append("    category: ").append(toIndentedString(category)).append("\n");
        sb.append("    code: ").append(toIndentedString(code)).append("\n");
        sb.append("    path: ").append(toIndentedString(path)).append("\n");
        sb.append("    text: ").append(toIndentedString(text)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    /**
     * Convert the given object to string with each line indented by 4 spaces
     * (except the first line).
     */
    private String toIndentedString(java.lang.Object o) {
        if (o == null) {
            return "null";
        }
        return o.toString().replace("\n", "\n    ");
    }

    /**
     * Gets or Sets code
     */
    public enum CodeEnum {
        UNKNOWN("TOKEN_UNKNOWN");

        private String value;

        CodeEnum(String value) {
            this.value = value;
        }

        @JsonCreator
        public static CodeEnum fromValue(String text) {
            for (CodeEnum b : CodeEnum.values()) {
                if (String.valueOf(b.value).equals(text)) {
                    return b;
                }
            }
            return null;
        }

        @Override
        @JsonValue
        public String toString() {
            return String.valueOf(value);
        }
    }
}

