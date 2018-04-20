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
import java.io.InputStream;
import java.net.URL;
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
    public String validate(@PathParam("url") String url) throws Exception {

        //return Json.createObjectBuilder().add("url", url).build();
        // return "<root>" + url + "</root>";

        /*
        Carl Wilson 2016-12-29: The best place to look for an example is here:

        https://github.com/veraPDF/veraPDF-integration-tests/blob/integration/src/test/java/org/verapdf/integration/CorpusTest.java#L60

        The appropriate Maven include is here:

        https://github.com/veraPDF/veraPDF-integration-tests/blob/integration/pom.xml#L83.
         */
        System.err.println("in routine");
        String flavorId = "1b";
        boolean prettyXml = true;
        PDFAFlavour flavour = PDFAFlavour.byFlavourId(flavorId);
        PDFAValidator validator = Foundries.defaultInstance().createValidator(flavour, false);
        try (InputStream inputStream = new URL(url).openStream();
             PDFAParser loader = Foundries.defaultInstance().createParser(inputStream, flavour)) {
            ValidationResult result = validator.validate(loader);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            XmlSerialiser.toXml(result, baos, prettyXml, false);
            final byte[] byteArray = baos.toByteArray();
            return new String(byteArray, StandardCharsets.UTF_8);  // string encoding not yet debugged
//        } catch (Exception e) {
//            throw new RuntimeException("validation failed", e);
//        } catch (EncryptedPdfException e) {
//            throw new RuntimeException("Encrypted PDF at " + url, e);
//        } catch (ModelParsingException e) {
//            e.printStackTrace();
//        } catch (MalformedURLException e) {
//            throw new RuntimeException("MalformedURLException for " + url, e);
//        } catch (IOException e) {
//            throw new RuntimeException()
//        } catch (ValidationException e) {
//            throw new RuntimeException("validation exception " + e.getMessage(), e);
//        } catch (JAXBException e) {
//            throw new RuntimeException("JAXBException: " + e.getMessage(), e);
        } catch (Exception e) {
            e.printStackTrace(System.err);
            return e.getMessage();
        }
    }

}
