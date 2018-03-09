package cart;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.makris.site.entities.CartItem;
import com.makris.site.entities.ShoppingItem;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class CartTest {
    private static final Logger logger = LogManager.getLogger();

    @Test
    public void cartItemUnpackedCheck() throws IOException{
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        Resource resources = resolver.getResource("classpath:test.json");
        CartItem cartItem = new ObjectMapper().readValue(resources.getFile(), CartItem.class);
        ShoppingItem item = cartItem.getShoppingItem();
        assertEquals(cartItem.getAmount(), 2);
        assertEquals(item.getCategory(), "3c");
        logger.info(cartItem.toString());
    }
}
