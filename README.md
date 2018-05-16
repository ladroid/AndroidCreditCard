# AndroidCreditCard
Using Stripe API
Here I used PayPal and Stripe API
In PayPalWallet I used Stripe API(I forgot to change the name).
How I used it?
At first add to gradle this 
```
compile 'com.stripe:stripe-android:6.1.2'
compile 'com.stripe:stripe-java:5.35.0'
```
The second thing is to add credit card input. So we must to add
CardMultilineWidget
This is xml file with CardMultilineWidget
``` xml
    <com.stripe.android.view.CardMultilineWidget
        android:id="@+id/cc"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        />
```
After this we can work with activity.
Here I will show simple things like: get information.
```java
card.validateNumber();
card.validateCVC();
```
With valiation we understand is this card real or not. For example we can check it like here
```java
if(!card.validateCard()) {
    ...
}
```
The next thing is getting information form credit card. How we cand do this? It's very easy.
```java
String cardNumber = card.getNumber();
Integer cardExpMonth = card.getExpMonth();
Integer cardExpYear = card.getExpYear();
String cardCVC = card.getCVC();
card.setNumber(cardNumber);
card.setExpMonth(cardExpMonth);
card.setExpYear(cardExpYear);
card.setCVC(cardCVC);
```
I think I will not explain what this code do cause everythig here is understandable.
Also using a Stripe we can send all information to server and from server see all our clients in workspace(if you registered there). You can make server using NodeJS or PHP. 
But how it works? Well everything I mean all main informaion is generated to token and then this token is sending to server. It's easy.
I think that's all.
Maybe sometimes I will updated my code and this short article.
