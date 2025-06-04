import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

import com.silanis.esl.api.model.SenderImageSignature;
import com.silanis.esl.api.model.Signer;
import com.silanis.esl.sdk.DocumentPackage;
import com.silanis.esl.sdk.EslClient;
 
public class SignUsingImage {
  public static final String CONFIG_PATH = "/home/john/Documents/OSS/config.properties";
  public static final String FILE_PATH = "/home/john/Documents/OSS//docs/capture_transaction.pdf";
  public static final String PACKAGE_TITLE = "Capture-Transaction";
  public static final String SIGNER = "john.cyclist.mcguinness+capture@gmail.com";
  public byte[] inputFileContentEncoded;
  public SenderImageSignature resultAfterUpdate;
  public SenderImageSignature resultAfterDelete;
  public static final String FILE_NAME = "exampleFile.jpg";
 
  public static void main(String[] args) throws IOException {
    String env = "US2.SKF";
    Properties prop = readPropertiesFile(CONFIG_PATH);
 
    EslClient eslClient = new EslClient(prop.getProperty(env + ".API"), prop.getProperty(env + ".URL"));
 
 
    Signer signer1 = newSignerWithEmail(SIGNER)
              .withCustomId("signer1")
              .withFirstName("John")
              .withLastName("Jones")
            .build();
    DocumentPackage superDuperPackage = newPackageNamed(PACKAGE_TITLE)
              .describedAs("This is a package created using OneSpan Sign SDK")
              .withSigner(signer1)
              .withDocument(newDocumentWithName("First Document")
                  .fromStream(documentInputStream1, DocumentType.PDF)
                  .withSignature(signatureFor(SIGNER)
                    .onPage(0)
                    .atPosition(100, 100)
                  )
                )
              .build();
 
    //     packageId = eslClient.createPackage( superDuperPackage );
    //     eslClient.sendPackage(packageId);
 
    //     eslClient.getSignatureImageService().getSignatureImageForSender(senderUID, PNG);
    //     eslClient.getSignatureImageService().getSignatureImageForPackageRole(packageId, signer1.getId(), JPG);
    //     PackageId packageId = eslClient.cre
   
    // atePackageOneStep(pkg);
    // eslClient.sendPackage(packageId);
 
    // System.out.println("{\n" + packageId + "\n}");
    // getSignerLink(eslClient.getPackageService().getSigningUrl(packageId, "Capture"), SIGNER);
  }
 
  // Print the link to Signing Ceremony
  public static void getSignerLink(String aSigner, String whom) {
    System.out.println("Link for " + whom + ":\n" + aSigner);
  }
 
  // Read account details from file
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