package testng;

import org.testng.annotations.Factory;

import java.io.File;
import java.util.Arrays;

import static util.TestNGUtil.getCucumberTest;

public class BaseReRunFeaturesTest {

    private static final String RERUN_DIR = "build/rerun";

    @Factory
    public Object[] createTests() {

        String[] rerunFiles = ( new File( RERUN_DIR ) ).list( ( dir, name ) -> !name.startsWith( "_init" )
                && name.endsWith( ".rerun" ) );
        assert rerunFiles != null;
        return Arrays.stream( rerunFiles ).map( this::createCucumberTest ).toArray();

    }


    private CucumberTest createCucumberTest(String rerunFile) {

        return getCucumberTest( rerunFile, null, true );

    }

}
