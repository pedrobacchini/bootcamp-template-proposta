package br.com.zup.bootcamp.proposta.util;

import org.junit.jupiter.params.provider.Arguments;
import org.springframework.mock.web.MockHttpServletResponse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.arguments;

public class IntegrationUtils {

    public static long getIdFromLocation(MockHttpServletResponse response) {
        var location = response.getHeader("Location");
        assertThat(location).isNotNull();
        return Long.parseLong(location.substring(location.lastIndexOf('/') + 1));
    }

    public static Arguments buildArguments(LinkedHashMap<String, Object> map, int index, Object invalidValue, String[] errorsFields, String[] errorsDetails) {
        var values = new ArrayList<>(map.values());
        values.set(index, invalidValue);
        values.addAll(Arrays.asList(errorsFields, errorsDetails));
        return arguments(values.toArray());
    }
}
