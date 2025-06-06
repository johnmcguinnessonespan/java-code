import java.time.LocalDateTime;
import java.util.HashMap;
import com.silanis.esl.sdk.DocumentPackage;
import com.silanis.esl.sdk.EslClient;
import com.silanis.esl.sdk.PackageId;
import com.silanis.esl.sdk.apitoken.ApiTokenConfig;
import com.silanis.esl.sdk.apitoken.ApiTokenConfig.TokenType;
import com.silanis.esl.sdk.builder.DocumentBuilder;
import com.silanis.esl.sdk.builder.PackageBuilder;
import com.silanis.esl.sdk.builder.SignatureBuilder;
import com.silanis.esl.sdk.builder.SignerBuilder;

public class UsingClientApp {
  public static final String PACKAGE_TITLE = "UsingClientApp: " + LocalDateTime.now();
  public static final String BASE_URL = "https://sandbox.esignlive.com";
  public static final String CLIENT_ID = "19745c2be1dcc337467bd1d37de";
  public static final String CLIENT_SECRET = "687964726191b0387b9df1aad66de00460659c460f31dbd4cab43151ff6925630c79855088";
  public static final String FILEPATH = "/home/john/Documents/OSS/docs/sampleAgreement.pdf";
  public static final String[] SIGNERS = { "john.cyclist.mcguinness+signer@gmail.com",
      "john.cyclist.mcguinness+reviewer@gmail.com" };

  @SuppressWarnings("rawtypes")
  public static void main(String[] args) {
    @SuppressWarnings("unchecked")
    EslClient eslClient = new EslClient(ApiTokenConfig.newBuilder()
        .clientAppId(CLIENT_ID)
        .clientAppSecret(CLIENT_SECRET)
        .baseUrl(BASE_URL)
        .tokenType(TokenType.OWNER)
        .build(),
        BASE_URL + "/api", false, null, false, new HashMap());

    // Build the DocumentPackage object
    DocumentPackage documentPackage = PackageBuilder
        .newPackageNamed(PACKAGE_TITLE)
        .withSigner(SignerBuilder.newSignerWithEmail(SIGNERS[0])
            .withFirstName("Signer")
            .withLastName("Signer")
            .withCustomId("Signer")
            .signingOrder(0)
            )
        .withSigner(SignerBuilder.newSignerWithEmail(SIGNERS[1])
            .withFirstName("Reviewer")
            .withLastName("Reviewer")
            .withCustomId("Reviewer")
            .signingOrder(1)
            )
        .withDocument(DocumentBuilder.newDocumentWithName("sampleAgreement")
            .fromFile(FILEPATH)
            .withSignature(SignatureBuilder
                .signatureFor(SIGNERS[0])
                .onPage(0)
                .atPosition(150, 165)
                )
            .withSignature(SignatureBuilder
                .signatureFor(SIGNERS[1])
                .onPage(0)
                .atPosition(475, 165)
                )
              )
        .build();

    // Issue the request to the OneSpan Sign server to create the DocumentPackage
    PackageId packageId = eslClient.createPackageOneStep(documentPackage);
    System.out.println(packageId);

    // Send the package to be signed by the participants
    eslClient.sendPackage(packageId);
    eslClient.signDocuments(packageId, "Signer");
    eslClient.signDocuments(packageId, "Reviewer");

//    getSignerLink(eslClient.getPackageService().getSigningUrl(packageId, "Signer"), SIGNERS[0]);
//    getSignerLink(eslClient.getPackageService().getSigningUrl(packageId, "Reviewer"), SIGNERS[1]);
  }

  public static void getSignerLink(String aSigner, String whom) {
    System.out.println("Link for " + whom + ":\n" + aSigner);
  }
}