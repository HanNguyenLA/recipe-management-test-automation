package dataproviders;

import org.testng.annotations.DataProvider;
import java.util.HashMap;
import java.util.Map;

public class RecipeDataProvider {

    // ==================== CUISINE DATA ====================
    public static class Cuisines {
        public static final String VIETNAMESE = "Việt Nam";
        public static final String CHINESE = "Trung Quốc";
        public static final String JAPANESE = "Nhật Bản";
        public static final String KOREAN = "Hàn Quốc";
        public static final String THAI = "Thái Lan";
        public static final String FRENCH = "Pháp";
        public static final String ITALIAN = "Ý";
        public static final String AMERICAN = "Mỹ";
    }

    // ==================== VALID DATA ====================
    @DataProvider(name = "validRecipeData")
    public static Object[][] getValidRecipeData() {
        return new Object[][] {
                {
                        "Phở Bò Hà Nội",
                        Cuisines.VIETNAMESE,
                        "Món phở truyền thống Việt Nam với nước dùng thơm ngon",
                        "4",
                        "120",
                        new String[]{"250g thịt bò", "200g bánh phở", "1 củ hành tây", "2 tép tỏi"},
                        new String[]{"Nấu nước dùng từ xương bò", "Luộc bánh phở", "Thái thịt mỏng", "Trình bày và thưởng thức"}
                },
                {
                        "Sushi Cá Hồi",
                        Cuisines.JAPANESE,
                        "Sushi cuộn cá hồi tươi ngon",
                        "2",
                        "30",
                        new String[]{"200g cá hồi", "100g cơm sushi", "Rong biển nori"},
                        new String[]{"Nấu cơm sushi", "Cuộn sushi với rong biển", "Cắt miếng vừa ăn"}
                },
                {
                        "Kimchi Jjigae",
                        Cuisines.KOREAN,
                        "Món canh kimchi cay nồng đặc trưng Hàn Quốc",
                        "3",
                        "45",
                        new String[]{"300g kimchi", "200g thịt ba chỉ", "1 hộp đậu phụ", "2 muỗng tương ớt Gochujang"},
                        new String[]{"Xào thịt ba chỉ", "Thêm kimchi và nước", "Cho đậu phụ vào nấu", "Nêm nếm và hoàn thành"}
                },
                {
                        "Pad Thai",
                        Cuisines.THAI,
                        "Món phở xào kiểu Thái với vị chua ngọt đặc trưng",
                        "2",
                        "25",
                        new String[]{"200g bún tươi", "100g tôm", "2 quả trứng", "Đậu phụng rang"},
                        new String[]{"Xào tôm với trứng", "Thêm bún và xào đều", "Nêm nếm với nước mắm Thái", "Rắc đậu phụng"}
                }
        };
    }

    // ==================== INVALID DATA ====================
    @DataProvider(name = "invalidRecipeData")
    public static Object[][] getInvalidRecipeData() {
        return new Object[][] {
                // Empty title
                {
                        "",
                        Cuisines.VIETNAMESE,
                        "Description",
                        "4",
                        "60",
                        new String[]{"Ingredient 1"},
                        new String[]{"Step 1"},
                        "Tên công thức không được để trống"
                },
                // Empty cuisine
                {
                        "Recipe Name",
                        "",
                        "Description",
                        "4",
                        "60",
                        new String[]{"Ingredient 1"},
                        new String[]{"Step 1"},
                        "Vui lòng chọn ẩm thực"
                },
                // Invalid serve (0)
                {
                        "Recipe Name",
                        Cuisines.VIETNAMESE,
                        "Description",
                        "0",
                        "60",
                        new String[]{"Ingredient 1"},
                        new String[]{"Step 1"},
                        "Số người phải lớn hơn 0"
                },
                // Invalid time (negative)
                {
                        "Recipe Name",
                        Cuisines.VIETNAMESE,
                        "Description",
                        "4",
                        "-10",
                        new String[]{"Ingredient 1"},
                        new String[]{"Step 1"},
                        "Thời gian phải lớn hơn 0"
                }
        };
    }

    // ==================== BOUNDARY DATA ====================
    @DataProvider(name = "boundaryData")
    public static Object[][] getBoundaryData() {
        return new Object[][] {
                // Min values
                {
                        "A", // 1 char title
                        Cuisines.VIETNAMESE,
                        "Short desc",
                        "1", // Min serve
                        "1", // Min time
                        new String[]{"Ing"},
                        new String[]{"Stp"}
                },
                // Max values
                {
                        "A".repeat(200), // Long title
                        Cuisines.VIETNAMESE,
                        "A".repeat(500), // Long description
                        "100", // Large serve
                        "9999", // Large time
                        new String[]{"Ingredient " + 1, "Ingredient " + 2, "Ingredient " + 3, "Ingredient " + 4, "Ingredient " + 5},
                        new String[]{"Step " + 1, "Step " + 2, "Step " + 3, "Step " + 4, "Step " + 5, "Step " + 6}
                }
        };
    }

    // ==================== SPECIAL CHARACTERS DATA ====================
    @DataProvider(name = "specialCharData")
    public static Object[][] getSpecialCharData() {
        return new Object[][] {
                {
                        "Món @n đ€c biệt #1",
                        Cuisines.VIETNAMESE,
                        "Mô tả có ký tự đặc biệt: @#$%^&*()",
                        "4",
                        "60",
                        new String[]{"250g thịt @ $5.99", "Muối & tiêu"},
                        new String[]{"Bước 1: Chuẩn bị (10 phút)", "Bước 2: Chế biến"}
                }
        };
    }

    // ==================== MULTIPLE INGREDIENTS/STEPS ====================
    @DataProvider(name = "multipleIngredientsData")
    public static Object[][] getMultipleIngredientsData() {
        return new Object[][] {
                // 2 ingredients (default)
                {2, new String[]{"Ingredient 1", "Ingredient 2"}},
                // 5 ingredients
                {5, new String[]{"Ing 1", "Ing 2", "Ing 3", "Ing 4", "Ing 5"}},
                // 10 ingredients
                {10, generateArray("Ingredient", 10)}
        };
    }

    @DataProvider(name = "multipleStepsData")
    public static Object[][] getMultipleStepsData() {
        return new Object[][] {
                {2, new String[]{"Step 1", "Step 2"}},
                {5, new String[]{"Step 1", "Step 2", "Step 3", "Step 4", "Step 5"}},
                {10, generateArray("Step", 10)}
        };
    }

    // ==================== CUISINE DROPDOWN DATA ====================
    @DataProvider(name = "allCuisines")
    public static Object[][] getAllCuisines() {
        return new Object[][] {
                {Cuisines.VIETNAMESE},
                {Cuisines.CHINESE},
                {Cuisines.JAPANESE},
                {Cuisines.KOREAN},
                {Cuisines.THAI},
                {Cuisines.FRENCH},
                {Cuisines.ITALIAN},
                {Cuisines.AMERICAN}
        };
    }

    // ==================== HELPER METHOD ====================
    private static String[] generateArray(String prefix, int count) {
        String[] array = new String[count];
        for (int i = 0; i < count; i++) {
            array[i] = prefix + " " + (i + 1);
        }
        return array;
    }

    // ==================== COMPLETE RECIPE DATA ====================
    public static Map<String, Object> getCompleteRecipeData() {
        Map<String, Object> data = new HashMap<>();
        data.put("title", "Bún Chả Hà Nội");
        data.put("cuisine", Cuisines.VIETNAMESE);
        data.put("description", "Món ăn đặc sản Hà Nội với thịt nướng thơm phức");
        data.put("serve", "4");
        data.put("time", "90");
        data.put("ingredients", new String[]{
                "300g thịt nạc vai",
                "200g thịt ba chỉ",
                "500g bún tươi",
                "Rau sống: xà lách, húng, rau thơm"
        });
        data.put("steps", new String[]{
                "Ướp thịt với gia vị trong 30 phút",
                "Nướng thịt trên than hồng",
                "Pha nước mắm chua ngọt",
                "Trình bày bún, thịt, rau sống và nước mắm"
        });
        data.put("imagePath", System.getProperty("user.dir") + "/test-data/images/bun-cha.jpg");
        return data;
    }
}