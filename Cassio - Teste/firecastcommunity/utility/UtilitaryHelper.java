package com.example.gabriela.firecastcommunity.utility;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

/**
 * Created by k on 29/08/2015.
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class UtilitaryHelper {

	public static EditText RetornarEditTextPorId(Activity activity, int id) {
		return (EditText) activity.findViewById(id);
	}

	public static TextView RetornarTextViewPorId(Activity activity, int id) {
		return (TextView) activity.findViewById(id);
	}

	public static Button RetornarButtonPorId(Activity activity, int id) {
		return (Button) activity.findViewById(id);
	}

	public static NumberPicker RetornarNumberPickerPorId(Activity activity,
			int id) {
		return (NumberPicker) activity.findViewById(id);
	}

	public static Spinner RetornarSpinnerPorId(Activity activity, int id) {
		return (Spinner) activity.findViewById(id);
	}

	public static CheckBox RetornarCheckBoxPorId(Activity activity, int id) {
		return (CheckBox) activity.findViewById(id);
	}

	public static RadioButton RetornarRadioButtonPorId(Activity activity, int id) {
		return (RadioButton) activity.findViewById(id);
	}

	public static TimePicker RetornarTimePickerPorId(Activity activity, int id) {
		return (TimePicker) activity.findViewById(id);
	}

	public static LinearLayout RetornarLinearLayoutPorId(Activity activity,
			int id) {
		return (LinearLayout) activity.findViewById(id);
	}

	public static <T> T RetornarObjetoSerializado(Activity activity) {
		Bundle param = activity.getIntent().getExtras();
		return (T) param.getSerializable(Constant.Value);
	}

	public static <T> T RetornarObjetoSerializado(Bundle param) {
		return (T) param.getSerializable(Constant.Value);
	}

	public static <T> T RetornarObjetoSerializado(Intent intent) {
		Bundle param = intent.getExtras();
		return (T) param.getSerializable(Constant.Value);
	}

	public static String RetornarTextoEditText(EditText objeto) {
		return objeto.getText().toString();
	}

	public static <T> Intent criarIntentComParametros(Activity activity,
			Class redirecionamento, T objeto) {
		Intent it = new Intent(activity, redirecionamento);
		it.putExtra(Constant.Value, (Serializable) objeto);
		return it;
	}

	public static <T> Intent criarIntentComParametrosSemNavegar(T objeto) {
		Intent it = new Intent();
		it.putExtra(Constant.Value, (Serializable) objeto);
		return it;
	}

	public static <T> Intent criarIntentENavegar(Activity activity,
			Class redirecionamento) {
		return new Intent(activity, redirecionamento);
	}

	public static NumberPicker ConfigurarNumberPicker(Activity activity,
			int id, int valorMinimo, int valorMaximo, int valorInicial) {
		NumberPicker number = RetornarNumberPickerPorId(activity, id);
		number.setMinValue(valorMinimo);
		number.setMaxValue(valorMaximo);
		number.setValue(valorInicial);
		return number;
	}

	public static TimePicker ConfigurarTimePicker24Horas(Activity activity,
			int id, int hora, int minuto) {
		TimePicker time = RetornarTimePickerPorId(activity, id);
		time.setIs24HourView(true);
		time.setCurrentHour(hora);
		time.setCurrentMinute(minuto);
		return time;
	}

	public static TimePicker ConfigurarTimePicker24Horas(Activity activity,
			int id, Date data) {
		TimePicker time = RetornarTimePickerPorId(activity, id);
		time.setIs24HourView(true);
		time.setCurrentHour(data.getHours());
		time.setCurrentMinute(data.getMinutes());
		return time;
	}

	public static NumberPicker ConfigurarNumberPicker(Activity activity,
			int id, int valorMinimo, int valorMaximo) {
		NumberPicker number = RetornarNumberPickerPorId(activity, id);
		number.setMinValue(valorMinimo);
		number.setMaxValue(valorMaximo);
		number.setValue(valorMinimo);
		return number;
	}

	public static int RetornarValorNumberPicker(NumberPicker number) {
		return number.getValue();
	}

	public static double RetornarDoubleDeUmEditText(EditText doubleEmTexto) {
		return Double.parseDouble(UtilitaryHelper
				.RetornarTextoEditText(doubleEmTexto));
	}

	public static int RetornarIntegerDeUmEditText(EditText intEmTexto) {
		return Integer.parseInt(UtilitaryHelper.RetornarTextoEditText(intEmTexto));
	}

	public static boolean RetornarRadioButtonValor(RadioButton radio) {
		return radio.isChecked();
	}

	public static boolean RetornarCheckBoxValor(CheckBox checkbox) {
		return checkbox.isChecked();
	}

	public static <T> T RetornarSelecionadoSpinner(Spinner spinner) {
		return (T) spinner.getSelectedItem();
	}

	public static String inserirZeroNaFrente(int valor) {
		return valor < 10 ? "0" + valor : String.valueOf(valor);
	}

	public static View GerarTextViewLayoutDinamico(Activity activity,
			String mensagem) {
		TextView textView = new TextView(activity);
		textView.setText(mensagem);
		return textView;
	}

	public static View GerarTextViewLayoutDinamico(Activity activity,
			String mensagem, int cor) {
		TextView textView = new TextView(activity);
		textView.setText(mensagem);
		textView.setTextColor(cor);
		return textView;
	}

	public static boolean ValorVazioOuNulo(String valorSelecionado) {
		if (valorSelecionado == null) {
			return true;
		}
		return valorSelecionado.isEmpty();
	}

	public static double ConversaoNumeroEditText(String numeroEmTexto) {
		return Double.parseDouble(numeroEmTexto);
	}

	public static void LimparEditText(EditText editText) {
		editText.setText("");
	}

	public static void LimparNumberPicker(NumberPicker number, int valor) {
		number.setValue(valor);
	}

	public static String EvitaNullPointer(String nome) {
		return nome != null ? nome : "";
	}

	public static String EvitaNullPointer(Date data) {
		return data != null ? ConverterDataEmString(data) : "";
	}

	public static String ConverterDataEmString(Date data) {
		if (data == null) {
			return "";
		}
		return UtilitaryHelper.inserirZeroNaFrente(data.getDate()) + "/"
				+ UtilitaryHelper.inserirZeroNaFrente((data.getMonth() + 1)) + "/"
				+ (data.getYear() + 1900);
	}

	public static String ConverterDataEHoraEmString(Date data) {
		if (data == null) {
			return "";
		}
		return UtilitaryHelper.inserirZeroNaFrente(data.getDate()) + "/"
				+ UtilitaryHelper.inserirZeroNaFrente((data.getMonth() + 1)) + "/"
				+ (data.getYear() + 1900) + " ("
				+ UtilitaryHelper.inserirZeroNaFrente(data.getHours()) + ":"
				+ UtilitaryHelper.inserirZeroNaFrente(data.getMinutes()) + ")";
	}

	public static Date ConverterStringEmData(String dataTexto) {
		DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		Date date;
		try {
			date = formatter.parse(dataTexto);
		} catch (ParseException e) {
			date = null;
		}
		return date;
	}

	public static Date ConverterStringEmData(EditText editTextData) {
		return ConverterStringEmData(UtilitaryHelper
				.RetornarTextoEditText(editTextData));
	}

	public static <T> boolean IsNotNull(T objeto) {
		return objeto != null;
	}

//	public static <T> int RetornaPosicaoElementoNoSpinner(Activity activity,
//			List<T> lista, T objetoSalvo) {
//		int posicao = -1;
//		int i = lista.size() > 1 ? 1 : 0;
//		for (T objeto : lista) {
//			if (((Entidade) objeto).getId() == ((Entidade) objetoSalvo).getId()) {
//				posicao = i;
//				break;
//			}
//			i++;
//		}
//		return posicao;
//	}

	public static int BuscarIdDoElementoNoSpinner(int objeto,
			ArrayAdapter<Integer> arrayAdapter) {
		return arrayAdapter.getPosition(objeto);
	}

	public static double RetornarDoubleDeUmTextView(TextView doubleEmTexto) {
		return Double.parseDouble(UtilitaryHelper
				.RetornarTextoTextView(doubleEmTexto));
	}

	public static String RetornarTextoTextView(TextView textoView) {
		return textoView.getText().toString();
	}

	public static String ApresentarComCasasDecimais(Double valor,
			int casaDecimais) {
		String texto = String.valueOf(valor);
		if (texto.contains(".")) {
			texto = texto.concat("0000000000");
			int posicaoPonto = texto.indexOf(".");
			String antesDoPonto = texto.substring(0, posicaoPonto);
			String depoisDoPonto = texto.substring(posicaoPonto, (posicaoPonto
					+ casaDecimais + 1));
			return antesDoPonto + depoisDoPonto;
		}
		return "0";
	}

//	public static String ValidarSpinner(Activity activity, int idLabelSpinner,
//			Spinner spinner) {
//		Entidade entidade = (Entidade) UtilitaryHelper
//				.RetornarSelecionadoSpinner(spinner);
//		if (entidade.getId() == 0) {
//			return activity.getString(idLabelSpinner) + ": "
//					+ activity.getString(R.string.spinner_deve_ser_selecionado);
//		}
//		return null;
//	}

//	public static String CampoObrigatorio(Activity activity, int idElemento,
//			EditText editText) {
//		if (IsNotNullAndNotEmpty(editText)) {
//			return null;
//		}
//		return activity.getString(idElemento) + ": "
//				+ activity.getString(R.string.campo_obrigatorio);
//	}

	public static boolean IsNotNullAndNotEmpty(EditText editText) {
		return StringContemInformacao(UtilitaryHelper
				.RetornarTextoEditText(editText));
	}

	public static boolean IsNotNullAndNotEmpty(TextView textView) {
		return StringContemInformacao(UtilitaryHelper
				.RetornarTextoTextView(textView));
	}

	public static boolean StringContemInformacao(String texto) {
		if (texto != null) {
			if (!texto.isEmpty()) {
				return true;
			}
		}
		return false;
	}

	public static long DiferencaDatas(Date dataInicio, Date dataFim) {
		return (dataFim.getTime() - dataInicio.getTime()) / (1000 * 60);
	}

	public static long DiferencaTimeLong(long timeInicio, long timeFim) {
		return (timeFim - timeInicio) / (1000 * 60);
	}

	public static boolean DataInicioEFimNaoAtendemABusca(long dataOcorrencia,
			Date dataInicio, Date dataFim) {
		if (dataInicio != null) {
			if (dataOcorrencia < dataInicio.getTime()) {
				return true;
			}
		}

		if (dataFim != null) {
			if (dataFim.getTime() < dataOcorrencia) {
				return true;
			}
		}
		return false;
	}

	public static boolean FiltroCheckBoxNaoAtendeBusca(
			boolean checkBoxAvaliado, long campoModelo, int valorComparado) {
		if (!checkBoxAvaliado) {
			if (campoModelo == valorComparado) {
				return true;
			}
		}
		return false;
	}

	public static boolean FiltroSpinnerNaoAtendeBusca(long valorIdSpinner,
			long idComparado) {
		if (valorIdSpinner > 0) {
			if (valorIdSpinner != idComparado) {
				return true;
			}
		}
		return false;
	}

	public static boolean FiltroCheckBoxNaoAtendeBusca(
			boolean checkBoxAvaliado, boolean campoModelo) {
		if (checkBoxAvaliado) {
			return !campoModelo;
		}
		return campoModelo;
	}

	public static String ValidarInteger(String valor, String nomeCampo,
			boolean permiteNulo, int valorMinimo, int valorMaximo) {
		if (!permiteNulo) {
			if (!IsNotNull(valor) || valor.isEmpty()) {
				return nomeCampo + ": Campo obrigat�rio";
			}
		}

		if (!IsNotNull(valor) || valor.isEmpty()) {
			return null;
		}
		
		int numero = Integer.parseInt(valor);

		if (numero < valorMinimo) {
			return nomeCampo + ": Deve ser superior a " + (valorMinimo - 1);
		}
		if (numero > valorMaximo) {
			return nomeCampo + ": Deve ser inferior a " + (valorMaximo + 1);
		}
		return null;
	}
}