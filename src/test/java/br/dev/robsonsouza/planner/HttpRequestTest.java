package br.dev.robsonsouza.planner;

import java.util.List;
import java.util.UUID;

import com.jayway.jsonpath.JsonPath;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class HttpRequestTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @Test
    void createTripTest() throws Exception {
        
        MvcResult mvcResult = createTrip();
        String id = JsonPath.read(mvcResult.getResponse().getContentAsString(), "$.id");
        Assertions.assertThat(UUID.fromString(id).toString()).hasToString(id);
    }
    @Test
    void updateTripTest() throws Exception {
        
        MvcResult mvcResult = createTrip();
        String id = JsonPath.read(mvcResult.getResponse().getContentAsString(), "$.id");
        Assertions.assertThat(UUID.fromString(id).toString()).hasToString(id);
        updateTrip(id);
        getTripRequest(id).andExpect(status().isOk())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.destination", notNullValue()))
                .andExpect(jsonPath("$.owner_name", notNullValue()))
                .andExpect(jsonPath("$.owner_email", notNullValue()))
                .andExpect(jsonPath("$.starts_at", notNullValue()))
                .andExpect(jsonPath("$.ends_at", notNullValue()))
                .andExpect(jsonPath("$.is_confirmed", notNullValue()))
                .andReturn();
    }
    
    @Test
    void createTripParticipantTest() throws Exception {
        
        MvcResult mvcResult = createTrip();
        String id = JsonPath.read(mvcResult.getResponse().getContentAsString(), "$.id");
        
        MvcResult participantResponse = getParticipantResponse(id, 1);
        List<String> participantList =
                JsonPath.read(participantResponse.getResponse().getContentAsString(), "$[*].id");
        
        String participant = JsonPath.read(mvcResult.getResponse().getContentAsString(), "$.id");
        Assertions.assertThat(UUID.fromString(id).toString()).hasToString(id);
    }
    
    @Test
    void confirmParticipantTest() throws Exception {
        
        MvcResult mvcResult = createTrip();
        String id = JsonPath.read(mvcResult.getResponse().getContentAsString(), "$.id");
        
        MvcResult participantResponse = getParticipantResponse(id, 1);
        List<String> participantList =
                JsonPath.read(participantResponse.getResponse().getContentAsString(), "$[*].id");
        getConfirmParticipant(participantList.get(0)).andExpect(status().isOk())
                .andExpect(jsonPath("$.name", hasToString("Mayk Brito")))
                .andExpect(jsonPath("$.isConfirmed", hasToString("true")))
                .andReturn();
        //
        
        Assertions.assertThat(UUID.fromString(id).toString()).hasToString(id);
    }
    
    @Test
    void inviteParticipantTest() throws Exception {
        
        MvcResult mvcResult = createTrip();
        String id = JsonPath.read(mvcResult.getResponse().getContentAsString(), "$.id");
        this.mockMvc.perform(post("/api/v1/trips/%s/invite".formatted(id)).contentType(
                        MediaType.APPLICATION_JSON).content(getParticipantWithEmailBody()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andReturn();
        getParticipantResponse(id, 2);
    }
    
    @Test
    void confirmParticipant_NotFoundTest() throws Exception {
        
        getConfirmParticipant(UUID.randomUUID().toString()).andExpect(status().isNotFound())
                .andReturn();
    }
    
    @Test
    void createActivityTest() throws Exception {
        MvcResult mvcResult = createTrip();
        String id = JsonPath.read(mvcResult.getResponse().getContentAsString(), "$.id");
        Assertions.assertThat(UUID.fromString(id).toString()).hasToString(id);
        MvcResult activityResposne = getActivityRegisterRequest(id).andExpect(status().isOk())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andReturn();
        String idActivity =
                JsonPath.read(activityResposne.getResponse().getContentAsString(), "$.id");
        
        getAllActivityRequest(id).andExpect(status().isOk())
                .andExpect(jsonPath("$[*].id", hasSize(1)))
                .andExpect(jsonPath("$[*].id", containsInAnyOrder(idActivity)))
                .andExpect(jsonPath("$[*].occurs_at", hasSize(1)))
                .andReturn();
    }
    
    @Test
    void createLinkTest() throws Exception {
        MvcResult mvcResult = createTrip();
        String id = JsonPath.read(mvcResult.getResponse().getContentAsString(), "$.id");
        Assertions.assertThat(UUID.fromString(id).toString()).hasToString(id);
        MvcResult activityResposne = getLinkRegisterRequest(id).andExpect(status().isOk())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andReturn();
        String idActivity =
                JsonPath.read(activityResposne.getResponse().getContentAsString(), "$.id");
        
        getAllLinksRequest(id).andExpect(status().isOk())
                .andExpect(jsonPath("$[*].id", hasSize(1)))
                .andExpect(jsonPath("$[*].id", containsInAnyOrder(idActivity)))
                .andExpect(jsonPath("$[*].url", hasSize(1)))
                .andReturn();
    }
    
    private ResultActions getConfirmParticipant(String id) throws Exception {
        return this.mockMvc.perform(
                post("/api/v1/participants/%s/confirm".formatted(id)).contentType(
                        MediaType.APPLICATION_JSON).content(getParticipantBody()));
    }
    
    private ResultActions getActivityRegisterRequest(String id) throws Exception {
        return this.mockMvc.perform(post("/api/v1/trips/%s/activities".formatted(id)).contentType(
                MediaType.APPLICATION_JSON).content(getActivityBody()));
    }
    
    private ResultActions getLinkRegisterRequest(String id) throws Exception {
        return this.mockMvc.perform(
                post("/api/v1/trips/%s/links".formatted(id)).contentType(MediaType.APPLICATION_JSON)
                        .content(getLinkBody()));
    }
    
    private ResultActions getAllActivityRequest(String id) throws Exception {
        return this.mockMvc.perform(get("/api/v1/trips/%s/activities".formatted(id)));
    }
    
    private ResultActions getAllLinksRequest(String id) throws Exception {
        return this.mockMvc.perform(get("/api/v1/trips/%s/links".formatted(id)));
    }
    
    private MvcResult getParticipantResponse(String id, int size) throws Exception {
        return getParticipantRequest(id).andExpect(status().isOk())
                .andExpect(jsonPath("$[*].id", hasSize(size)))
                .andReturn();
    }
    
    private ResultActions getParticipantRequest(String id) throws Exception {
        return this.mockMvc.perform(get("/api/v1/trips/%s/participants".formatted(id)));
    }
    
    @Test
    public void getTrip() throws Exception {
        
        MvcResult mvcResult = createTrip();
        String id = JsonPath.read(mvcResult.getResponse().getContentAsString(), "$.id");
        
        getTripRequest(id).andExpect(status().isOk())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.destination", notNullValue()))
                .andExpect(jsonPath("$.owner_name", notNullValue()))
                .andExpect(jsonPath("$.owner_email", notNullValue()))
                .andExpect(jsonPath("$.starts_at", notNullValue()))
                .andExpect(jsonPath("$.ends_at", notNullValue()))
                .andExpect(jsonPath("$.is_confirmed", notNullValue()))
                .andReturn();
        Assertions.assertThat(UUID.fromString(id).toString()).hasToString(id);
    }
    
    @Test
    void getTripConfirmTest() throws Exception {
        
        MvcResult mvcResult = createTrip();
        String id = JsonPath.read(mvcResult.getResponse().getContentAsString(), "$.id");
        
        getTripConfirmRequest(id).andExpect(status().isOk())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.destination", notNullValue()))
                .andExpect(jsonPath("$.owner_name", notNullValue()))
                .andExpect(jsonPath("$.owner_email", notNullValue()))
                .andExpect(jsonPath("$.starts_at", notNullValue()))
                .andExpect(jsonPath("$.ends_at", notNullValue()))
                .andExpect(jsonPath("$.is_confirmed", notNullValue()))
                .andReturn();
        Assertions.assertThat(UUID.fromString(id).toString()).hasToString(id);
    }
    
    @Test()
    public void getTrip_NotFound() throws Exception {
        getTripRequest(UUID.randomUUID().toString()).andExpect(status().isNotFound()).andReturn();
    }
    
    @Test()
    public void getTripConfirm_NotFound() throws Exception {
        getTripConfirmRequest(UUID.randomUUID().toString()).andExpect(status().isNotFound())
                .andReturn();
    }
    
    private ResultActions getTripRequest(String id) throws Exception {
        return this.mockMvc.perform(
                get("/api/v1/trips/%s".formatted(id)).accept(MediaType.APPLICATION_JSON));
    }
    
    private ResultActions getTripConfirmRequest(String id) throws Exception {
        return this.mockMvc.perform(
                get("/api/v1/trips/%s/confirm".formatted(id)).accept(MediaType.APPLICATION_JSON));
    }
    
    private MvcResult createTrip() throws Exception {
        return this.mockMvc.perform(post("/api/v1/trips").contentType(MediaType.APPLICATION_JSON)
                                            .content(getCreateBody()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andReturn();
    }
    
    private MvcResult updateTrip(String id) throws Exception {
        return this.mockMvc.perform(put("/api/v1/trips/%s".formatted(id)).contentType(MediaType.APPLICATION_JSON)
                                            .content(getCreateBody()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andReturn();
    }
    String getCreateBody() {
        return "{ \"destination\": \"Florianópolis, SC\", \"starts_at\": \"2024-06-20T21:51:54.7342\", \"ends_at\": \"2024-06-25T21:51:54.7342\", \"emails_to_invite\": [\"mayk.brito@rocketseat.com\"], \"owner_name\": \"Fernanda Kipper\", \"owner_email\": \"fernanda.kipper@rocketseat.com\"}";
    }
    
    String getParticipantBody() {
        return "{\"name\":\"Mayk Brito\"}";
    }
    
    String getParticipantWithEmailBody() {
        return "{\"name\":\"Diego Fernandes\", \"email\":\"diego.schell@rocketseat.com\"}";
    }
    
    String getActivityBody() {
        return "{\"title\":\"Passeio ao Cristo Redentor\",\"occurs_at\": \"2024-06-25T21:51:54.7342\"}";
    }
    
    String getLinkBody() {
        return "{\"title\": \"Termos do airbnb\",\"url\": \"https://www.airbnb.com.br/terms\"}";
    }
}
