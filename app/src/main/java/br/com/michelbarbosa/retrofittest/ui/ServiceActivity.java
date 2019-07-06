package br.com.michelbarbosa.retrofittest.ui;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import br.com.michelbarbosa.retrofittest.domain.RespostaServidor;
import br.com.michelbarbosa.retrofittest.service.RetrofitService;
import br.com.michelbarbosa.retrofittest.service.ServiceGenerator;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ServiceActivity extends AppCompatActivity {

    RespostaServidor resposta = new RespostaServidor();
    ProgressDialog progress;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public void listenersButtons() {

    }

    public void setValues() {

    }

    public void getValues() {

    }

    /**
     * O tão aguardado método que demos o nome de retrofitConverter é aonde a mágica acontece na activity.
     * Quando o botão de enviar é clicado, é chamado este método e passado para eles os parâmetros de entrada do endpoint.
     * É instanciado um objeto do serviço que criamos, e em seguida, é passado os parâmetros para o método getCardById
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
     * de progresso pois a requisição acabou e chamamos o método setValues().
     */
    public void retrofitConverter(String cardId) {
        RetrofitService service = ServiceGenerator.createService(RetrofitService.class);
        Toast.makeText(getApplicationContext(), cardId + " Inserted", Toast.LENGTH_SHORT).show();
        Call<RespostaServidor> call = service.getCardById(cardId);

        call.enqueue(new Callback<RespostaServidor>() {
            @Override
            public void onResponse(Call<RespostaServidor> call, Response<RespostaServidor> response) {

                if (response.isSuccessful()) {
                    RespostaServidor respostaServidor = response.body();

                    //verifica aqui se o corpo da resposta não é nulo
                    if (respostaServidor != null) {

                        Log.d("BODY: ", respostaServidor.toString());

                        resposta.setName(respostaServidor.getName());
                        resposta.setDbfId(respostaServidor.getCost());
                        resposta.setCardSet(respostaServidor.getCardSet());
                        resposta.setCardId(respostaServidor.getCardId());

                        progress.dismiss();
                        setValues();

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
