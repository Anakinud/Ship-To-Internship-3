package com.idemia;

import com.idemia.http.dto.AisleDto;
import com.idemia.http.dto.PersonDto;
import com.idemia.http.dto.PersonType;
import com.idemia.http.dto.PlaceDto;
import com.idemia.http.dto.PutDto;
import com.idemia.http.dto.SeatDto;
import com.idemia.http.dto.SeatingPolicyDto;
import com.idemia.model.Preference;
import com.idemia.model.Unoccupied;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;

import static com.idemia.http.dto.PersonType.NORMAL;
import static com.idemia.http.dto.PersonType.VIP;
import static com.idemia.model.Preference.AISLE;
import static com.idemia.model.Preference.WINDOW;
import static com.idemia.model.SeatingPolicy.FROM_BACK;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.PUT;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = AFTER_EACH_TEST_METHOD)
public class FifthTest {
    @Autowired
    private TestRestTemplate client;

    @Test
    public void shouldInitializePlane() {
        List<List<PlaceDto>> places = List.of(
                List.of(getSeat(), getAisle(), getSeat()),
                List.of(getSeat(), getAisle(), getSeat())
        );
        ResponseEntity<String> response = client.postForEntity(
                "/plane",
                places,
                String.class);

        //then
        assertThat(response.getStatusCode()).isEqualTo(OK);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        //and
        ResponseEntity<List<List<PlaceDto>>> getResponse = getStatus(headers);

        assertThat(getResponse.getStatusCode()).isEqualTo(OK);
        assertThat(getResponse.getBody()).isEqualTo(places);
    }

    @Test
    public void shouldAdd() {
        List<List<PlaceDto>> places = List.of(
                List.of(getSeat(), getAisle(), getSeat()),
                List.of(getSeat(), getAisle(), getSeat())
        );
        client.postForEntity(
                "/plane",
                places,
                String.class);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        //when
        ResponseEntity<String> getResponse = client.postForEntity(
                "/plane/persons",
                new PutDto(NORMAL),
                String.class);

        //then
        assertThat(getResponse.getStatusCode()).isEqualTo(OK);

        //and
        ResponseEntity<List<List<PlaceDto>>> status = getStatus(headers);
        assertThat(status.getStatusCode()).isEqualTo(OK);
        assertThat(status.getBody()).isEqualTo(List.of(
                List.of(getSeat(NORMAL), getAisle(), getSeat()),
                List.of(getSeat(), getAisle(), getSeat())
        ));
    }

    @Test
    public void shouldAddWithPreference() {
        List<List<PlaceDto>> places = List.of(
                List.of(getSeat(), getAisle(), getSeat(), getAisle(), getSeat(), getSeat(), getSeat(), getAisle(), getSeat()),
                List.of(getSeat(), getAisle(), getSeat(), getAisle(), getSeat(), getSeat(), getSeat(), getAisle(), getSeat())
        );
        client.postForEntity(
                "/plane",
                places,
                String.class);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        //when
        put(NORMAL, AISLE);

        put(NORMAL, WINDOW);

        put(NORMAL, AISLE);

        put(NORMAL, WINDOW);

        put(NORMAL, WINDOW);

        put(NORMAL, WINDOW);

        put(NORMAL);

        //then
        ResponseEntity<List<List<PlaceDto>>> status = getStatus(headers);
        assertThat(status.getStatusCode()).isEqualTo(OK);
        assertThat(status.getBody()).isEqualTo(List.of(
                        List.of(
                                getSeat(NORMAL),
                                getAisle(),
                                getSeat(NORMAL),
                                getAisle(),
                                getSeat(NORMAL),
                                getSeat(NORMAL),
                                getSeat(),
                                getAisle(),
                                getSeat(NORMAL)),
                        List.of(getSeat(NORMAL), getAisle(), getSeat(), getAisle(), getSeat(), getSeat(), getSeat(), getAisle(), getSeat(NORMAL))
                )
        );
    }

    @Test
    public void differentOrder() {
        List<List<PlaceDto>> places = List.of(
                List.of(getSeat(), getAisle(), getSeat(), getAisle(), getSeat(), getSeat(), getSeat(), getAisle(), getSeat()),
                List.of(getSeat(), getAisle(), getSeat(), getAisle(), getSeat(), getSeat(), getSeat(), getAisle(), getSeat())
        );
        client.postForEntity(
                "/plane",
                places,
                String.class);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        //when
        ResponseEntity<String> response = client.exchange(
                "/plane/seating-policy",
                PUT,
                new HttpEntity<>(new SeatingPolicyDto(FROM_BACK)),
                String.class
        );

        //then
        assertThat(response.getStatusCode()).isEqualTo(CREATED);

        put(NORMAL, AISLE);

        put(NORMAL, WINDOW);

        put(NORMAL, AISLE);

        put(NORMAL, WINDOW);

        put(NORMAL, WINDOW);

        put(NORMAL, WINDOW);

        put(NORMAL);

        //then
        ResponseEntity<List<List<PlaceDto>>> status = getStatus(headers);
        assertThat(status.getStatusCode()).isEqualTo(OK);
        assertThat(status.getBody()).isEqualTo(List.of(
                List.of(getSeat(NORMAL), getAisle(), getSeat(), getAisle(), getSeat(), getSeat(), getSeat(), getAisle(), getSeat(NORMAL)),
                List.of(
                        getSeat(NORMAL),
                        getAisle(),
                        getSeat(),
                        getAisle(),
                        getSeat(NORMAL),
                        getSeat(NORMAL),
                        getSeat(NORMAL),
                        getAisle(),
                        getSeat(NORMAL))
        ));
    }

    @Test
    public void unoccupiedSigned() {
        List<List<PlaceDto>> places = List.of(
                List.of(getSeat(), getAisle(), getSeat()),
                List.of(getSeat(), getAisle(), getSeat()),
                List.of(getSeat(), getAisle(), new SeatDto(new PersonDto(VIP)))
        );
        client.postForEntity(
                "/plane",
                places,
                String.class);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        //when
        ResponseEntity<Unoccupied> response = client.getForEntity(
                "/plane/unoccupied",
                Unoccupied.class);

        //then
        assertThat(response.getStatusCode()).isEqualTo(OK);

        //and
        assertThat(response.getBody().getNumber()).isEqualTo(5);
        assertThat(response.getBody().getSignature()).isEqualTo("13B6E347CE626AA618C7B2FA419FA8CB9D38E8018B2AFEAB58EEDEF0EAD235B8");
    }

    private void put(PersonType typ, Preference preference) {
        client.postForEntity(
                "/plane/persons",
                new PutDto(typ, preference),
                String.class);
    }

    private void put(PersonType typ) {
        put(typ, null);
    }

    private ResponseEntity<List<List<PlaceDto>>> getStatus(HttpHeaders headers) {
        return client.exchange(
                "/plane",
                GET,
                new HttpEntity<>(headers),
                new ParameterizedTypeReference<>() {
                });
    }

    private AisleDto getAisle() {
        return new AisleDto();
    }

    private SeatDto getSeat() {
        return new SeatDto();
    }

    private SeatDto getSeat(PersonType personType) {
        return new SeatDto(new PersonDto(personType));
    }

}
