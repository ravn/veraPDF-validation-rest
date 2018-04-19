package dk.kb.dpa.verapdf.rest;

import org.verapdf.core.XmlSerialiser;
import org.verapdf.pdfa.Foundries;
import org.verapdf.pdfa.PDFAParser;
import org.verapdf.pdfa.PDFAValidator;
import org.verapdf.pdfa.VeraGreenfieldFoundryProvider;
import org.verapdf.pdfa.flavours.PDFAFlavour;
import org.verapdf.pdfa.results.ValidationResult;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 *
 */
@Path("validate")
public class JAXRSValidator {

    // We need to tell VeraPDF which provider to use.
    {
        {
            VeraGreenfieldFoundryProvider.initialise();
        }
    }

    @GET
    @Path("{url}")
    @Produces(MediaType.APPLICATION_XML)
    public String validate(@PathParam("url") String url) {

        //return Json.createObjectBuilder().add("url", url).build();
        // return "<root>" + url + "</root>";

        /*
        Carl Wilson 2016-12-29: The best place to look for an example is here:

        https://github.com/veraPDF/veraPDF-integration-tests/blob/integration/src/test/java/org/verapdf/integration/CorpusTest.java#L60

        The appropriate Maven include is here:

        https://github.com/veraPDF/veraPDF-integration-tests/blob/integration/pom.xml#L83.

        TRA 2017-01-23:  Froze pom.xml ranges at version 1.0.6
         */
        String flavorId = "1b";
        boolean prettyXml = true;
        PDFAFlavour flavour = PDFAFlavour.byFlavourId(flavorId);
        PDFAValidator validator = Foundries.defaultInstance().createValidator(flavour, false);
        try (InputStream inputStream = new FileInputStream("/home/tra/Downloads/unittesting.pdf");
             PDFAParser loader = Foundries.defaultInstance().createParser(inputStream, flavour)) {
            ValidationResult result = validator.validate(loader);

            // do in-memory generation of XML byte array - as we need to pass it to Fedora we need it to fit in memory anyway.

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            XmlSerialiser.toXml(result, baos, prettyXml, false);
            final byte[] byteArray = baos.toByteArray();
            return new String(byteArray, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException("validation failed", e);
        }
    }

}
