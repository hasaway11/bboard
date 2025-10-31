package lecture;

enum Fruit {
  BANANA, ORANGE;
}
public class EnumTest {
  public static void main(String[] args) {
    // 후식 과일을 선택 : 바나나, 오렌지
    // 범위중에 하나를 선택해야 할 때 일반 변수로 처리하면 문제가 발생
    String 후식 = "바나나";
    String 내후식 = "수박";

    if((내후식.equals("바나나") || 내후식.equals("오렌지"))==false) {
      System.out.println("후식은 바나나 또는 오렌지만 선택가능합니다");
    }

    // enum을 사용하면
    Fruit fruit = Fruit.BANANA;
  }
}
