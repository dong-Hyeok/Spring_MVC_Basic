package springmvc.simpleservice.domain.repository;

import org.springframework.stereotype.Repository;
import springmvc.simpleservice.domain.item.Item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ItemRepository {

    // 실제 개발에서는 동시성 접근으로 ConcurrentHashMap 와

    private static final Map<Long, Item> store = new HashMap<>(); // static
    private static long sequence = 0L;

    public Item save(Item item) {
        item.setId(++sequence);
        store.put(item.getId(), item);

        return item;
    }

    public Item findById(Long id) {
        return store.get(id);
    }

    public List<Item> findAll() {
        return new ArrayList<>(store.values());
    }

    public void update(Long itemId, Item updateItem) {
        Item findItem = findById(itemId);
        findItem.setItemName(updateItem.getItemName());
        findItem.setPrice(updateItem.getPrice());
        findItem.setQuantity(updateItem.getQuantity());
    }

    public void clearStore() {
        store.clear();
    }
}
