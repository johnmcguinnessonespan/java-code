import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
//import java.util.Map;
import java.util.Properties;


//import com.google.common.collect.Sets;
import com.silanis.esl.sdk.EslClient;
import com.silanis.esl.sdk.FieldSummary;
import com.silanis.esl.sdk.PackageId;
//import com.silanis.esl.sdk.PackageStatus;
//import com.silanis.esl.sdk.Page;
//import com.silanis.esl.sdk.PageRequest;

// Get a list of fields used in a Transactions
public class ListTFields {
  public static final String CONFIG_PATH = "/home/john/Documents/OSS/config.properties";
  public static final String PACKAGE_TITLE = "List-Packages";
  public static final String TRANSACTION_ID = "nf7-H15gKfy_N3hKeLmXziw6ga0=";  
  public static void main(String[] args) throws IOException {
    String env = "US2.SKF";
    Properties prop = readPropertiesFile(CONFIG_PATH);

    EslClient eslClient = new EslClient(prop.getProperty(env + ".API"), prop.getProperty(env + ".URL"));

    // supply the package ID
    PackageId packageId = new PackageId( TRANSACTION_ID );
 
     List<FieldSummary> fieldSummaries = eslClient.getFieldValues( packageId );
 
    System.out.println( "SignerId,\t DocumentId, \tFieldId \tFieldName \tValue" );
    
    for ( FieldSummary fieldSummary : fieldSummaries ) {
      System.out.print( fieldSummary.getSignerId() + ", \t" +
        fieldSummary.getDocumentId() + ", \t" +
        fieldSummary.getFieldId() + ", \t" +
        fieldSummary.getFieldName() + ", \t" +
        fieldSummary.getFieldValue() + "\n" );
    }
  }

  // private static Page<DocumentPackage> getPackagesByPackageStatus(PackageStatus packageStatus, Date startDate, Date endDate)
  //   {
  //   Page<DocumentPackage> resultPage = eslClient.getPackageService().getUpdatedPackagesWithinDateRange(packageStatus, new PageRequest(1), startDate, endDate);
    
  //   return resultPage;
  //   }

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