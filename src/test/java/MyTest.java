import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.RandomUtil;
import com.bend.AppStarter;
import com.bend.dao.ArticleMapper;
import com.bend.dao.UserMapper;
import com.bend.entity.Article;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = AppStarter.class)
public class MyTest {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ArticleMapper articleMapper;

    @Test
    public void t() {
        for (int i = 0; i < 50; i++) {
            Article article = new Article();
            article.setAid(IdUtil.fastSimpleUUID());
            article.setFirst("first"+i);
            article.setSecond("second");
            article.setBelong(RandomUtil.randomInt(0,10));
            article.setType(1);
            articleMapper.insert(article);
        }
    }


}
