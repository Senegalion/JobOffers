package pl.joboffers.controller.error;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import pl.joboffers.BaseIntegrationTest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class OfferUrlDuplicateExceptionIntegrationTest extends BaseIntegrationTest {
    private static final String ADD_OFFER_ENDPOINT = "/offers";

    @Test
    public void should_return_status_409_conflict_when_added_second_offer_with_the_same_offer_url() throws Exception {
        // step 1 - adding new offer
        // given
        // when
        ResultActions perform = mockMvc.perform(post(ADD_OFFER_ENDPOINT)
                .content("""
                        {
                            "companyName":"testCompany",
                            "position":"testPosition",
                            "salary":"5000 - 8000 PLN",
                            "offerUrl":"https://joboffers/testOffers/1"
                        }
                        """.trim()
                )
                .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        perform.andExpect(status().isCreated());

        // step 2 - adding the same offer (which already exists)
        // given
        // when
        ResultActions performForAddingNewOffer = mockMvc.perform(post(ADD_OFFER_ENDPOINT)
                .content("""
                        {
                            "companyName":"testCompany",
                            "position":"testPosition",
                            "salary":"5000 - 8000 PLN",
                            "offerUrl":"https://joboffers/testOffers/1"
                        }
                        """.trim()
                )
                .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        performForAddingNewOffer.andExpect(status().isConflict());
    }
}
