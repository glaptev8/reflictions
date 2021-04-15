package edu.school21.classes.car;

import java.sql.SQLOutput;

public class Car {
  String mark;
  Integer price;
  Boolean isSale;

  public Car() {}

  public Car(String mark, Integer price, Boolean isSale) {
    this.mark = mark;
    this.price = price;
    this.isSale = isSale;
  }

  public void goCar (Integer speed) {
    System.out.println(mark + "go with speed = " + speed);
  }

  public Integer getPriceWithSale () {
    if (isSale) {
      return price / 100 * 80;
    }
    return price;
  }

  @Override
  public String toString() {
    return "Car{" +
      "mark='" + mark + '\'' +
      ", price=" + price +
      ", isSale=" + isSale +
      '}';
  }
}
