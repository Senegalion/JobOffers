package pl.joboffers.apivalidationerror;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import pl.joboffers.BaseIntegrationTest;
import pl.joboffers.infrastructure.apivalidation.ApiValidationErrorDto;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ApiValidationFailedIntegrationTest extends BaseIntegrationTest {
    private static final String ADD_OFFER_ENDPOINT = "/offers";
    public static final String COMPANY_NAME_MUST_NOT_BE_NULL = "COMPANY NAME MUST NOT BE NULL";
    public static final String POSITION_MUST_NOT_BE_NULL = "POSITION MUST NOT BE NULL";
    public static final String SALARY_MUST_NOT_BE_NULL = "SALARY MUST NOT BE NULL";
    public static final String OFFER_URL_MUST_NOT_BE_NULL = "OFFER_URL MUST NOT BE NULL";
    public static final String COMPANY_NAME_MUST_NOT_BE_EMPTY = "COMPANY NAME MUST NOT BE EMPTY";
    public static final String POSITION_MUST_NOT_BE_EMPTY = "POSITION MUST NOT BE EMPTY";
    public static final String SALARY_MUST_NOT_BE_EMPTY = "SALARY MUST NOT BE EMPTY";
    public static final String OFFER_URL_MUST_NOT_BE_EMPTY = "OFFER_URL MUST NOT BE EMPTY";

    @Test
    public void should_return_400_bad_request_and_validation_message_when_request_is_empty() throws Exception {
        // given
        // when
        ResultActions perform = mockMvc.perform(post(ADD_OFFER_ENDPOINT)
                .content("""
                        {
                        }
                        """.trim()
                )
                .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        MvcResult mvcResult = perform.andExpect(status().isBadRequest()).andReturn();
        String json = mvcResult.getResponse().getContentAsString();
        ApiValidationErrorDto result = objectMapper.readValue(json, ApiValidationErrorDto.class);
        assertThat(result.errorMessages()).containsExactlyInAnyOrder(
                COMPANY_NAME_MUST_NOT_BE_NULL,
                POSITION_MUST_NOT_BE_NULL,
                SALARY_MUST_NOT_BE_NULL,
                OFFER_URL_MUST_NOT_BE_NULL,
                COMPANY_NAME_MUST_NOT_BE_EMPTY,
                POSITION_MUST_NOT_BE_EMPTY,
                SALARY_MUST_NOT_BE_EMPTY,
                OFFER_URL_MUST_NOT_BE_EMPTY
        );
    }
}
