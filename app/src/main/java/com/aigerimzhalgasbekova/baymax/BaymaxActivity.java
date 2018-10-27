package com.aigerimzhalgasbekova.baymax;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class BaymaxActivity extends AppCompatActivity {

    private QuizLibrary quizLibrary = new QuizLibrary();

    private TextView mQuestionView;
    private Button mButtonChoice1;
    private Button mButtonChoice2;
    private TextView mResultView;
    private List<String> userInput = new LinkedList<>();

    private int mQuestionNumber = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_baymax);

        mQuestionView = (TextView)findViewById(R.id.question);
        mButtonChoice1 = (Button)findViewById(R.id.choice1);
        mButtonChoice2 = (Button)findViewById(R.id.choice2);
        mResultView = (TextView)findViewById(R.id.result);

        updateQuestion();

        //Start of Button Listener for Button1
        mButtonChoice1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                StringBuilder sb = new StringBuilder(mButtonChoice1.getText().length());
                sb.append(mButtonChoice1.getText());
                userInput.add(sb.toString());

                if (mQuestionNumber < quizLibrary.size()){
                    updateQuestion();
                } else {
                    clear();
                }
            }
        });

        //End of Button Listener for Button1

        //Start of Button Listener for Button2
        mButtonChoice2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                StringBuilder sb = new StringBuilder(mButtonChoice2.getText().length());
                sb.append(mButtonChoice1.getText());
                userInput.add(sb.toString());
                //Log.i("qNum", quizLibrary.size() + "");
                if (mQuestionNumber < quizLibrary.size()){
                    updateQuestion();
                } else {
                    clear();
                }
            }
        });

    }

    private void clear() {
        mQuestionView.setText("");
        mButtonChoice1.setText("");
        mButtonChoice2.setText("");
        mResultView.setText(calculateBestOptions().toString());
    }

    private void updateQuestion() {

        Log.i("qNum", quizLibrary.size() + "");
        mQuestionView.setText(quizLibrary.getQuestion(mQuestionNumber));
        mButtonChoice1.setText(quizLibrary.getChoice1(mQuestionNumber));
        mButtonChoice2.setText(quizLibrary.getChoice2(mQuestionNumber));

        mQuestionNumber++;
    }

    private Set<Map<String, Object>> calculateBestOptions() {
        String[] inputArray = new String[userInput.size()];
        Metrics metrics = Metrics.with()
                                 .userInput(inputArray)
                                 .partnerProducts(new ProductsDeserializer().deserialize(loadJSONFromAsset()))
                                 .build();

        Classifier classifier = new Classifier(metrics);

        HashMap<Integer, Double> productsPriorities = classifier.prioritirize();
        Map<Integer, Double> sorted = sortByValues(productsPriorities);
        Set<Integer> sortedKeys = sorted.keySet();
        List<Map<String, Object>> productList = new LinkedList<>();
        Set<Map<String, Object>> result = new LinkedHashSet<>();
        productList.addAll(metrics.getParterProoducts());
        for (Integer key : sortedKeys) {
            result.add(productList.get(key));
        }
        return result;
    }
    public String loadJSONFromAsset() {
        String json = null;
        try {
            //Log.i("InputStream", is.toString());
            InputStream is = this.getAssets().open("Products.json");
            int size = is.available();

            byte[] buffer = new byte[size];

            is.read(buffer);

            is.close();

            json = new String(buffer, "UTF-8");


        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;

    }

    public static <K extends Comparable,V extends Comparable> Map<K,V> sortByValues(Map<K,V> map){
        List<Map.Entry<K,V>> entries = new LinkedList<Map.Entry<K,V>>(map.entrySet());

        Collections.sort(entries, new Comparator<Map.Entry<K,V>>() {

            @Override
            public int compare(Map.Entry<K, V> o1, Map.Entry<K, V> o2) {
                return o1.getValue().compareTo(o2.getValue());
            }
        });

        //LinkedHashMap will keep the keys in the order they are inserted
        //which is currently sorted on natural ordering
        Map<K,V> sortedMap = new LinkedHashMap<K, V>();

        for(Map.Entry<K,V> entry: entries){
            sortedMap.put(entry.getKey(), entry.getValue());
        }

        return sortedMap;
    }
}
