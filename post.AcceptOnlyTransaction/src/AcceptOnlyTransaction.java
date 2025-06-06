import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Properties;

import com.silanis.esl.sdk.DocumentPackage;
import com.silanis.esl.sdk.EslClient;
import com.silanis.esl.sdk.PackageId;
import com.silanis.esl.sdk.PackageStatus;
import com.silanis.esl.sdk.builder.DocumentBuilder;
import com.silanis.esl.sdk.builder.PackageBuilder;
import com.silanis.esl.sdk.builder.SignatureBuilder;
import com.silanis.esl.sdk.builder.SignerBuilder;

public class AcceptOnlyTransaction {
  public static final String PACKAGE_TITLE = "UsingClientApp: " + LocalDateTime.now();
  public static final String CONFIG_PATH = "/home/john/Documents/OSS/config.properties";
public static final String FILEPATH = "/home/john/Documents/OSS/docs/sampleAgreement.pdf";
  public static final String SIGNER =  "john.cyclist.mcguinness+signer@gmail.com";

  public static void main(String[] args) throws IOException {
    String env = "US2.SKF";
    Properties prop = readPropertiesFile(CONFIG_PATH);

    EslClient eslClient = new EslClient(prop.getProperty(env + ".API"), prop.getProperty(env + ".URL"));

    // Build the DocumentPackage object
    PackageBuilder pkg =  PackageBuilder.newPackageNamed("OSS Test Package JavaSDK")
      .withStatus(PackageStatus.SENT)
      .withSigner(SignerBuilder.newSignerWithEmail(SIGNER)
        .withCustomId("DHL_SIGNERR")                        
        .withFirstName(prop.getProperty("FORENAME"))
        .withLastName("Signed"))
      .withDocument(DocumentBuilder.newDocumentWithName("sampleAgreement")
        .withId("BOB")                        
        .fromFile(FILEPATH)
        .withSignature(SignatureBuilder.acceptanceFor(SIGNER))
        );

    DocumentPackage dp = pkg.build();    

    PackageId packageId = eslClient.createPackageOneStep(dp);

    // Do the AutoSign
    eslClient.signDocuments(packageId, "DHL_SIGNERR");
  }

  public static void getSignerLink(String aSigner, String whom) {
    System.out.println("Link for " + whom + ":\n" + aSigner);
  }

   // read account details from config file
  public static Properties readPropertiesFile(String fileName) throws IOException {
    FileInputStream fis = null;
    Properties prop = null;

    try {
      fis = new FileInputStream(fileName);
      prop = new Properties();
      prop.load(fis);
    } catch (FileNotFoundException fnfe) {
      fnfe.printStackTrace();
    } catch (IOException ioe) {
      ioe.printStackTrace();
    } finally {
      fis.close();
    }

    return prop;
  }
 
}