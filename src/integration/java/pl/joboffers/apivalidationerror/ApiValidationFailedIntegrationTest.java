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
                INPUT_NUMBERS_MUST_NOT_BE_EMPTY
        );
    }
}
