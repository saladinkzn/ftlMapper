import ru.shadam.ftlmapper.ast.ASTParser;
import org.junit.Test;

import java.lang.reflect.Type;
import java.util.List;

/**
 * @author Timur Shakurov
 */
public class Test1 {
    interface Repository {
        List<Integer> getAll();
    }

    @Test
    public void test1() throws Exception {
        final Type integerList = Repository.class.getMethod("getAll").getGenericReturnType();
        final ASTParser astParser = new ASTParser();
        System.out.println(astParser.parse("", "", integerList));
    }
}
