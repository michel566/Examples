package br.com.michelbarbosa.retrofittest.ui;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import br.com.michelbarbosa.retrofittest.R;

public class MainActivity extends ServiceActivity {
    private Button btSubmit;
    private EditText etCodCard;
    private TextView tvCardId, tvDbfId, tvCardSet, tvName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etCodCard = findViewById(R.id.etCod_card);
        tvCardId = findViewById(R.id.tvCardId);
        tvDbfId = findViewById(R.id.tvDbfId);
        tvCardSet = findViewById(R.id.tvCardSet);
        tvName = findViewById(R.id.tvName);
        btSubmit = findViewById(R.id.btSubmit);

        listenersButtons();
    }



    /**
     * Chama os listeners para os botões
     * No método de escuta do botão, temos outras três ações principais: primeiramente é instanciado um objeto da classe
     * ProgressDialog, que nada mais é que um diálogo para mostrar um loading de carregamento enquanto a conversa com o
     * webservice ainda não acabou. Na sequência são pegos os valores dos edittextos pois cada valor será um
     * parâmetro de entrada do nosso endpoint, como já explicado. Por fim chamamos o método que fará a mágica do Retrofit acontecer,
     * podendo assim consumir json no Android.
     */

    @Override
    public void listenersButtons() {
        btSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progress = new ProgressDialog(MainActivity.this);
                progress.setTitle("enviando...");
                progress.show();

                getValues();
            }
        });
    }

    /**
     * O último método da nossa classe diz respeito à setar os valores da resposta do servidor nos textviews.
     * Como já possuímos o valor da conversão no nosso novo objeto resposta, basta coloca-lo no textview.
     */

    @Override
    public void setValues() {
        tvName.setText(resposta.getName());
        tvDbfId.setText(resposta.getDbfId());
        tvCardId.setText(resposta.getCardId());
        tvCardSet.setText(resposta.getCardSet());
    }

    @Override
    public void getValues(){
        //pega os valores dos edittextos
        String cardId = etCodCard.getText().toString();

        //chama o retrofit para fazer a requisição no webservice
        retrofitConverter(cardId);
    }

 }
