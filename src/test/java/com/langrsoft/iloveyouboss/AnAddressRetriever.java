package com.langrsoft.iloveyouboss;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Tag;
import java.io.*;
import com.langrsoft.util.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
// START:test
// ...
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

// START:test
class AnAddressRetriever {
    // START_HIGHLIGHT
    Http http = mock(Http.class);
    // END_HIGHLIGHT

    @Test
    void answersAppropriateAddressForValidCoordinates()
        throws IOException {
        // START_HIGHLIGHT
        when(http.get(contains("lat=38.000000&lon=-104.000000"))).thenReturn(
            """
                {"address":{
                  "house_number":"324",
                  "road":"Main St",
                  "city":"Anywhere",
                  "state":"Colorado",
                  "postcode":"81234",
                  "country_code":"us"}}
                """);
        // END_HIGHLIGHT
        var retriever = new AddressRetriever(http);

        var address = retriever.retrieve(38, -104);
        // ...
        // END:test

        assertEquals("324", address.house_number());
        assertEquals("Main St", address.road());
        assertEquals("Anywhere", address.city());
        assertEquals("Colorado", address.state());
        assertEquals("81234", address.postcode());
        // START:test
    }
    // ...
// END:test

    // START:throws
    @Test
    void throwsWhenNotUSCountryCode() {
        when(http.get(anyString())).thenReturn("""
            {"address":{ "country_code":"not us"}}""");
        var retriever = new AddressRetriever(http);

        assertThrows(UnsupportedOperationException.class,
            () -> retriever.retrieve(1.0, -1.0));
    }
    // END:throws

    @Disabled("works as of 2024-Mar-24")
    @Tag("slow")
    @Test
    void liveIntegrationTest() throws IOException {
        var retriever = new AddressRetriever(new HttpImpl());

        var address = retriever.retrieve(38.8372956, -104.8255679);

        assertEquals("North Cascade Avenue", address.road());
        assertEquals("Colorado Springs", address.city());
        assertEquals("Colorado", address.state());
        assertEquals("80903", address.postcode());
    }
    // START:test
}
// END:test
