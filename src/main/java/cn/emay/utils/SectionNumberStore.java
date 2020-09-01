package cn.emay.utils;

import cn.emay.core.base.pojo.SectionNumber;
import cn.emay.utils.tree.EmayTreeNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

public class SectionNumberStore {
    private final Logger logger = LoggerFactory.getLogger(SectionNumberStore.class);
    private final EmayTreeNode<Character, SectionNumber> root = new EmayTreeNode<>('1', null);

    public SectionNumberStore(SectionNumber... numbers) {
        if (numbers == null || numbers.length == 0) {
            return;
        }
        Arrays.stream(numbers).forEach(this::save);
    }

    public void save(SectionNumber number) {
        if (number == null) {
            return;
        }
        char[] mobilenodes = number.getNumber().trim().toCharArray();
        EmayTreeNode<Character, SectionNumber> parent = root;
        for (char mobilenode : mobilenodes) {
            EmayTreeNode<Character, SectionNumber> node = parent.getChild(mobilenode);
            if (node == null) {
                node = new EmayTreeNode<>(mobilenode, null);
                parent.addChild(node, false);
            }
            parent = node;
        }
        parent.setFruit(number);
    }

    public SectionNumber getSectionInfo(String mobile) {
        String orginalMobile = mobile;
        if (mobile == null || mobile.trim().length() < 7) {
            return null;
        }
        mobile = mobile.substring(0, 7);
        EmayTreeNode<Character, SectionNumber> node = root;
        char[] mobilenodes = mobile.trim().toCharArray();
        for (char mobilenode : mobilenodes) {
            node = node.getChild(mobilenode);
            if (node == null) {
                break;
            }
        }
        if (node != null) {
            SectionNumber sectionNumber = node.getFruit();
            if (null == sectionNumber) {
                logger.info("号段为空！手机号为=" + orginalMobile + ",前7位=" + mobile);
            } else {
                if (!sectionNumber.getIsDelete()) {
                    return sectionNumber;
                } else {
                    return null;
                }
            }
        }
        return null;
    }

    public void delete(String mobile) {
        if (mobile == null || mobile.trim().length() != 11) {
            return;
        }
        EmayTreeNode<Character, SectionNumber> last = root;
        char[] mobilenodes = mobile.trim().toCharArray();
        for (char mobilenode : mobilenodes) {
            last = last.getChild(mobilenode);
            if (last == null) {
                return;
            }
        }
        do {
            last.getParent().removeChild(last.getId());
            last = last.getParent();
        } while (last != null && (last.getChildren() == null || last.getChildren().size() == 0) && (last.getParent() != null));
    }

    public static void main(String[] args) {
        SectionNumberStore s = new SectionNumberStore();
        SectionNumber st = new SectionNumber();
        st.setNumber("1567364");
        st.setProvinceCode("10");
        SectionNumber st1 = new SectionNumber();
        st1.setNumber("1543885");
        st1.setProvinceCode("11");
        SectionNumber st2 = new SectionNumber();
        st2.setNumber("1253885");
        st2.setProvinceCode("12");
        s.save(st);
        s.save(st1);
        s.save(st2);
        System.out.println(s.getSectionInfo("1567364").getProvinceCode());
        System.out.println(s.getSectionInfo("1543885").getProvinceCode());
        System.out.println(s.getSectionInfo("1253885").getProvinceCode());
        System.out.println(s.getSectionInfo("15538851").getProvinceCode());
        System.out.println(s.getSectionInfo("12231231231"));
        System.out.println(s.getSectionInfo("12538385"));
    }
}
