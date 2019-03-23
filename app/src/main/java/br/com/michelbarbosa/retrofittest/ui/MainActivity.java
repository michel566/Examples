package br.com.michelbarbosa.retrofittest.ui;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import br.com.michelbarbosa.retrofittest.R;
import br.com.michelbarbosa.retrofittest.domain.RespostaServidor;
import br.com.michelbarbosa.retrofittest.service.RetrofitService;
import br.com.michelbarbosa.retrofittest.service.ServiceGenerator;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private Button botaoEnviar;
    private EditText unidadeEntrada, unidadeSaida, valorEntrada;
    private TextView saidaValorConverter, saidaUnidadeConverter, saidaUnidadeConvertida, saidaValorConvertido;
    RespostaServidor resposta = new RespostaServidor();
    ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        unidadeEntrada = (EditText) findViewById(R.id.edittext_unidade_entrada);
        unidadeSaida = (EditText) findViewById(R.id.edittext_unidade_saida);
        valorEntrada = (EditText) findViewById(R.id.edittext_valor);
        saidaValorConverter = (TextView) findViewById(R.id.textview_valorconverter);
        saidaUnidadeConverter = (TextView) findViewById(R.id.textview_unidadeconverter);
        saidaUnidadeConvertida = (TextView) findViewById(R.id.textview_unidadeconvertida);
        saidaValorConvertido = (TextView) findViewById(R.id.textview_valorconvertido);
        botaoEnviar = (Button) findViewById(R.id.button_enviar);

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

    public void listenersButtons() {
        botaoEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progress = new ProgressDialog(MainActivity.this);
                progress.setTitle("enviando...");
                progress.show();

                //pega os valores dos edittextos
                String unidadeE = unidadeEntrada.getText().toString();
                String unidadeS = unidadeSaida.getText().toString();
                String valor = valorEntrada.getText().toString();

                //chama o retrofit para fazer a requisição no webservice
                retrofitConverter(unidadeE, valor, unidadeS);

            }
        });
    }

    /**
     * O último método da nossa classe diz respeito à setar os valores da resposta do servidor nos textviews.
     * Como já possuímos o valor da conversão no nosso novo objeto resposta, basta coloca-lo no textview.
     */

    public void setaValores() {
        saidaUnidadeConverter.setText(unidadeEntrada.getText().toString());
        saidaValorConverter.setText(valorEntrada.getText().toString());
        saidaUnidadeConvertida.setText(unidadeSaida.getText().toString());
        saidaValorConvertido.setText(resposta.getResult());
    }

    /**
     * O tão aguardado método que demos o nome de retrofitConverter é aonde a mágica acontece na activity.
     * Quando o botão de enviar é clicado, é chamado este método e passado para eles os parâmetros de entrada do endpoint.
     * É instanciado um objeto do serviço que criamos, e em seguida, é passado os parâmetros para o método converterUnidade
     * que declaramos lá na nossa interface.
     * <p>
     * Em seguida, temos o método onResponse do próprio Retrofit. Quando a requisição for completada e o
     * servidor já possuir uma resposta, iremos verificar se a resposta foi um sucesso. Após isso, jogamos o corpo (body) da
     * resposta dentro de um objeto dentro de um objeto RespostaServidor (isso graças ao GSON).
     * <p>
     * No retorno do servidor um dos campos é para dizer se os parâmetros de entradas foram válidos
     * e a conversão aconteceu com sucesso. Este campo chama-se valid e é um campo do tipo boolean.
     * Verificamos então se isValid é true. Caso seja, passamos todos os campos do objeto resposta
     * para um novo objeto RespostaSevidor, para que possamos manipulá-lo como quisermos. Por fim, retiramos o diálogo
     * de progresso pois a requisição acabou e chamamos o método setaValores().
     */
    public void retrofitConverter(String unidadeEnt, String valorEnt, String unidadeSai) {
        RetrofitService service = ServiceGenerator.createService(RetrofitService.class);
        Call<RespostaServidor> call = service.converterUnidade(unidadeEnt, valorEnt, unidadeSai);

        call.enqueue(new Callback<RespostaServidor>() {
            @Override
            public void onResponse(Call<RespostaServidor> call, Response<RespostaServidor> response) {

                if (response.isSuccessful()) {
                    RespostaServidor respostaServidor = response.body();

                    //verifica aqui se o corpo da resposta não é nulo
                    if (respostaServidor != null) {
                        if (respostaServidor.isValid()) {
                            resposta.setFrom_type(respostaServidor.getFrom_type());
                            resposta.setFrom_value(respostaServidor.getFrom_value());
                            resposta.setResult(respostaServidor.getResult());
                            resposta.setTo_type(respostaServidor.getTo_type());
                            resposta.setValid(respostaServidor.isValid());

                            progress.dismiss();
                            setaValores();

                        } else {
                            Toast.makeText(getApplicationContext(), "Insira unidade e valores válidos", Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Toast.makeText(getApplicationContext(), "Resposta nula do servidor", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(getApplicationContext(), "Resposta não foi sucesso", Toast.LENGTH_SHORT).show();
                    // segura os erros de requisição
                    ResponseBody errorBody = response.errorBody();
                }

                progress.dismiss();
            }

            @Override
            public void onFailure(Call<RespostaServidor> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Erro na chamada ao servidor", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
