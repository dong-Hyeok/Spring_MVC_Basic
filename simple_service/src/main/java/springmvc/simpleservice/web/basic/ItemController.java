package springmvc.simpleservice.web.basic;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import springmvc.simpleservice.domain.item.Item;
import springmvc.simpleservice.domain.repository.ItemRepository;

import java.util.List;

@Controller
@RequestMapping("/basic/items")
public class ItemController {

    private final ItemRepository itemRepository;

    @Autowired
    public ItemController(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    @GetMapping
    public String items(Model model) {
        List<Item> items = itemRepository.findAll();
        model.addAttribute("items", items);
        return "basic/items";
    }

    @GetMapping("/{itemId}")
    public String item(@PathVariable("itemId") long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "basic/item";
    }

    @GetMapping("/add")
    public String addForm() {
        return "basic/addForm";
    }

    //    @PostMapping("/add")
    public String addItemV1(
            @RequestParam("itemName") String itemName,
            @RequestParam("price") int price,
            @RequestParam("quantity") Integer quantity,
            Model model
    ) {

        Item item = new Item();
        item.setItemName(itemName);
        item.setPrice(price);
        item.setQuantity(quantity);

        itemRepository.save(item);

        model.addAttribute("item", item);

        return "basic/item";
    }

    //    @PostMapping("/add")
    public String addItemV2(@ModelAttribute("item") Item item, Model model) {

        itemRepository.save(item);
//      model.addAttribute("item", item); //자동 추가, 생략 가능

        return "basic/item";
    }

    //    @PostMapping("/add")
    public String addItemV3(@ModelAttribute Item item) { // (" ") 생략시 해당 객체 클래스명의 첫 글자를 소문자로 변경해서 취급
        itemRepository.save(item);
        return "basic/item";
    }

    //    @PostMapping("/add")
    public String addItemV4(Item item) { // ModelAttribute 생략 가능 V3와 동일하게 동작
        itemRepository.save(item);
        return "basic/item";
    }

    //    @PostMapping("/add")
    public String addItemV5(Item item) {
        itemRepository.save(item);

        // 영어는 상관없지만, 이와 같이 넣을 경우 한글 등 인코딩 문제가 발생할 수 있다.
        return "redirect:/basic/items/" + item.getId();
    }

    // RedirectAttributes
    @PostMapping("/add")
    public String addItemV6(Item item, RedirectAttributes redirectAttributes) {
        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true); // 파라미터로 설정
        return "redirect:/basic/items/{itemId}";
    }

    @GetMapping("/{itemId}/edit")
    public String editForm(@PathVariable("itemId") Long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "basic/editForm";
    }

    // editForm을 보면 th:action 하고 뒤에 값이 없다. 이는 현 주소와 동일하다는 의미이고 submit으로 현 주소에 post form 요청을 한 것이다.
    // 따라서 아래 post 함수가 실행되고 리다이렉트로 다시 화면을 이동한다.
    @PostMapping("/{itemId}/edit")
    public String edit(@PathVariable("itemId") Long itemId, @ModelAttribute Item item) {
        itemRepository.update(itemId, item);
        return "redirect:/basic/items/{itemId}";
    }

    /**
     * 테스트용 데이터 추가
     */
    @PostConstruct
    public void init() {
        itemRepository.save(new Item("itemA", 10000, 10));
        itemRepository.save(new Item("itemB", 20000, 20));
    }

}
