package com.just2fast.ushop;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

//import com.razorpay.Checkout;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BuyingDetails#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BuyingDetails extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public BuyingDetails() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BuyingDetails.
     */
    // TODO: Rename and change types and number of parameters
    public static BuyingDetails newInstance(String param1, String param2) {
        BuyingDetails fragment = new BuyingDetails();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    TextView fullAddressText ,phText,heading,sellingText,offText,countItems,
            increaseItems,reduceItems,sizeText,colorText,nameText,mrpText,sellingPrice,Discount,deliveryCharge,noOfOrderText,totalPrice;
    ImageView productImage1;
    Activity parent;
    int noOfProducts;
    ImageView backBtn;
    Button makePayment;
    int totalAmount;
    ArrayList<ProductModel> list = new ArrayList<>();
    String fullAddress,name,ph;

    ProductModel currentProduct ;
    
    String sellingPriceOverall;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_buying_details, container, false);

//        Checkout.preload(parent);

        makePayment = v.findViewById(R.id.makePayment);
        fullAddressText = v.findViewById(R.id.fullAddress);
        nameText = v.findViewById(R.id.name);
        countItems = v.findViewById(R.id.countItems);
        phText = v.findViewById(R.id.contact);
        reduceItems = v.findViewById(R.id.reduceItems);
        deliveryCharge = v.findViewById(R.id.deliveryCharge);
        heading = v.findViewById(R.id.heading);
        sizeText = v.findViewById(R.id.sizeText);
        backBtn = v.findViewById(R.id.backBtn);
        offText = v.findViewById(R.id.offText);

        totalPrice = v.findViewById(R.id.total);
        noOfOrderText = v.findViewById(R.id.noOfOrderText);
        Discount = v.findViewById(R.id.Discount);
        sellingPrice = v.findViewById(R.id.sellingPrice);
        mrpText = v.findViewById(R.id.mrpText);
        sellingText = v.findViewById(R.id.sellingPriceTextView);
        colorText = v.findViewById(R.id.colorText);
        increaseItems = v.findViewById(R.id.increaseItems);
        productImage1 = v.findViewById(R.id.productImage1);
        parent = getActivity();

        
        list = getArguments().getParcelableArrayList("list");
        currentProduct = list.get(0);

        SharedPreferences sp1 = parent.getSharedPreferences("BuyValues", Context.MODE_PRIVATE);
        String size121 = sp1.getString("ChooseSize","null");

        int posOfPriceSize = 0;

        for(int i = 0; i < currentProduct.size.size(); i++){
            if(size121.equals(currentProduct.size.get(i))){
                posOfPriceSize = i;
            }
        }
        
        sellingPriceOverall = currentProduct.sizePrice.get(posOfPriceSize);
        
        Log.d("SellingPriceInBuyPage",sellingPriceOverall);
         fullAddress = getArguments().getString("Address");
         ph = getArguments().getString("ph");
         name = getArguments().getString("name");

        SharedPreferences sharedPreferences =parent.getSharedPreferences("Charges",Context.MODE_PRIVATE);

        int serviceCharge = sharedPreferences.getInt("SERVICE",20);
        int shippingCharge = sharedPreferences.getInt("SHIPPING",10);




        increaseItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(noOfProducts!=50 && noOfProducts<currentProduct.stock ) {
                    countItems.setText(noOfProducts + 1 + "");
                    noOfProducts = noOfProducts + 1;
                    totalPrice.setText("₹"+(Integer.parseInt(sellingPriceOverall)*noOfProducts+serviceCharge+shippingCharge));
                    Discount.setText(noOfProducts+"");
                }
            }
        });



        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                parent.onBackPressed();
            }
        });



        reduceItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(noOfProducts!=1){
                    countItems.setText(noOfProducts-1+"");
                    noOfProducts = noOfProducts-1;
                    totalPrice.setText("₹"+(Integer.parseInt(sellingPriceOverall)*noOfProducts+serviceCharge+shippingCharge));
                    Discount.setText(noOfProducts+"");
                }
            }
        });





        nameText.setText(name);
        SharedPreferences sp = parent.getSharedPreferences("BuyValues", Context.MODE_PRIVATE);
        String size = sp.getString("ChooseSize","null");
        String color = sp.getString("ChooseColor","null");
        sizeText.setText("Size: "+size);
        colorText.setText("color: "+color);
        noOfProducts = sp.getInt("noOfProducts",1);
        countItems.setText(noOfProducts+"");

        int p = list.get(0).title.indexOf("--");

        if(p!=-1){
            heading.setText(list.get(0).title.substring(0,p));
        }
        else{
            heading.setText(list.get(0).title);
        }


        int posOfUri = 0;
        for (int i =0;i<list.get(0).color.size();i++){
            if(color.equals(list.get(0).color.get(i)) ){
                posOfUri = i;
            }
        }

        Picasso.get().load(list.get(0).colorImages.get(posOfUri)).into(productImage1);
        fullAddressText.setText(fullAddress);
        phText.setText(ph);
        int currentNoOfPro = (Integer.parseInt(sellingPriceOverall));
        sellingText.setText("₹"+currentNoOfPro);
        int value1 = Integer.parseInt(sellingPriceOverall);
        int value2 = Integer.parseInt(list.get(0).mrp);
        float discount = (value1/value2)*100;
        String offString = (100-discount+" ").substring(0,4)+"%off";
        offText.setText(offString);
        mrpText.setText(Integer.parseInt(sellingPriceOverall)+"");
        sellingPrice.setText(Integer.parseInt(list.get(0).mrp)-Integer.parseInt(sellingPriceOverall)+"");
       // Discount.setText(Integer.parseInt(list.get(0).mrp.replace(list.get(0).productId,""))-Integer.parseInt(sellingPriceOverall.replace(list.get(0).productId,"")));
        noOfOrderText.setText(shippingCharge+"");
        noOfProducts = Integer.parseInt(countItems.getText().toString());
        totalAmount =((Integer.parseInt(sellingPriceOverall)*noOfProducts)+serviceCharge+shippingCharge);
        totalPrice.setText("₹"+totalAmount);

        Discount.setText(noOfProducts+"");
        deliveryCharge.setText(serviceCharge+"");




        makePayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //startPayment();
                noOfProducts = Integer.parseInt(countItems.getText().toString());
                Bundle bundle = new Bundle();
                bundle.putString("name",name);
                bundle.putString("ph",ph);
                bundle.putString("address",fullAddress);
                bundle.putString("size",size);
                bundle.putString("color",color);
                bundle.putString("totalAmount",((Integer.parseInt(sellingPriceOverall)*noOfProducts)+serviceCharge+shippingCharge)+"");
                Log.d("total636",((Integer.parseInt(sellingPriceOverall)*noOfProducts)+serviceCharge+shippingCharge)+"");
                bundle.putInt("noOfProducts",noOfProducts);
                bundle.putParcelableArrayList("list",list);

                if(parent instanceof MainActivity){
                    ((MainActivity)parent).runFragment(bundle,new payment_method(),"Payment Method");
                }
                else {
                    ((search_instance)parent).runFragment(bundle,new payment_method(),5);
                }

            }
        });


        return v;
    }






//        /**
//         * Reference to current activity
//         */
//        final Activity activity = parent;
//
//        /**
//         * Pass your payment options to the Razorpay Checkout as a JSONObject
//         */
//        try {
//            JSONObject options = new JSONObject();
//
//            options.put("name", "Merchant Name");
//            options.put("description", "Reference No. #123456");
//            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.jpg");
//            options.put("order_id", "order_DBJOWzybf0sJbb");//from response of step 3.
//            options.put("theme.color", "#3399cc");
//            options.put("currency", "INR");
//            options.put("amount", "50000");//pass amount in currency subunits
//            options.put("prefill.email", "gaurav.kumar@example.com");
//            options.put("prefill.contact","9988776655");
//            JSONObject retryObj = new JSONObject();
//            retryObj.put("enabled", true);
//            retryObj.put("max_count", 4);
//            options.put("retry", retryObj);
//
//            checkout.open(activity, options);
//
//        } catch(Exception e) {
//            Log.e("ErrorInPayment", "Error in starting Razorpay Checkout", e);
//        }
//    }


}