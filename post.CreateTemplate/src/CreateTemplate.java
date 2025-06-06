import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import com.silanis.esl.sdk.DocumentPackage;
import com.silanis.esl.sdk.EslClient;
import com.silanis.esl.sdk.PackageId;
import com.silanis.esl.sdk.Placeholder;
import com.silanis.esl.sdk.Visibility;
import com.silanis.esl.sdk.builder.DocumentBuilder;
import com.silanis.esl.sdk.builder.PackageBuilder;
import com.silanis.esl.sdk.builder.SignatureBuilder;
import com.silanis.esl.sdk.builder.SignerBuilder;

public class CreateTemplate {
  public static final String CONFIG_PATH = "/home/john/Documents/OSS/config.properties";
  public static final String FILE_PATH = "/home/john/Documents/OSS/docs/create_template.pdf";
  public static final String PACKAGE_TITLE = "Create-Template";
  public static final String TEMPLATE_NAME = "CreatedTemplate";

  public static void main(String[] args) throws IOException {
    String env = "US1.SBX";
    Properties prop = readPropertiesFile(CONFIG_PATH);

    EslClient eslClient = new EslClient(prop.getProperty(env + ".API"), prop.getProperty(env + ".URL"));

    DocumentPackage documentPackage = PackageBuilder.newPackageNamed(PACKAGE_TITLE)
        .withVisibility(Visibility.ACCOUNT)
        // .withVisibility(Visibility.SENDER)
        .withSigner(SignerBuilder.newSignerPlaceholder(new Placeholder("Signer1")))
        .withSigner(SignerBuilder.newSignerPlaceholder(new Placeholder("Signer2")))
        .withDocument(DocumentBuilder.newDocumentWithName(TEMPLATE_NAME)
            .fromFile(FILE_PATH)
            .withSignature(SignatureBuilder.signatureFor(new Placeholder("Signer1"))
                .onPage(0)
                .atPosition(100, 140))
            .withSignature(SignatureBuilder.signatureFor(new Placeholder("Signer2"))
                .onPage(0)
                .atPosition(100, 205)))
        .build();

    PackageId templateId = eslClient.getTemplateService().createTemplate(documentPackage);

    System.out.println("{\n" + templateId + "\n}");
    System.out.println();
  }

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