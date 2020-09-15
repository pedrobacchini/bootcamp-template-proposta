package br.com.zup.bootcamp.proposta.util;

import org.springframework.mock.web.MockHttpServletResponse;

import static org.assertj.core.api.Assertions.assertThat;

public class MockUtils {

    public static long getIdFromLocation(MockHttpServletResponse response) {
        var location = response.getHeader("Location");
        assertThat(location).isNotNull();
        return Long.parseLong(location.substring(location.lastIndexOf('/') + 1));
    }
}
