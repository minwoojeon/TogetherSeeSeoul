package src.kit.code.binoo.togetherseeseoul.dummy;

import java.util.ArrayList;
import java.util.List;

import src.kit.code.binoo.togetherseeseoul.CulturalInfo;

public class DummyContent {

    public static final List<DummyItem> ITEMS = new ArrayList<DummyItem>();

    public static class DummyItem {
        public final Integer id;
        public final String title;
        public final CulturalInfo culturalInfo;

        public DummyItem(Integer id, String title, CulturalInfo culturalInfo) {
            this.id = id;
            this.title = title;
            this.culturalInfo = culturalInfo;
        }

        @Override
        public String toString() {
            return title;
        }
    }
}
