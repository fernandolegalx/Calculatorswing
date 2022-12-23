package modelo;

import java.util.ArrayList;
import java.util.List;

public class memoria {
	
	private enum TipoComando {
			ZERAR,SINAL, NUMERO, DIV, MULT, SOMA, IGUAL, VIRGULA, SUB;
	};
	private static final memoria instacia = new memoria();
	
	
	private TipoComando ultimaOperacao;
	private boolean substituir = false;
	private String textoAtual = "";
	private String textoBuffer = "";
	
	
	private final List<MemoriaObservador>observadores = 
			new ArrayList<>();
	
	private memoria() {
		
	}

	public static memoria getInstacia() {
		return instacia;
	}
	
	public void adicionarObservador(MemoriaObservador observador) {
	observadores.add(observador);
	
	}
	
	public String getTextoAtual() {
		return textoAtual.isEmpty() ? "0" : textoAtual;
	}
	
	public void processarComando(String texto) {
		
		TipoComando tipoComando = detectarTipoComando(texto);
		if(tipoComando == null) {
			return;
		}else if(tipoComando == TipoComando.ZERAR) {
			textoAtual = "";
			textoBuffer = "";
			substituir = false;
			ultimaOperacao = null;
		}else if(tipoComando == TipoComando.SINAL && textoAtual.contains("-")){
			textoAtual = textoAtual.substring(1);
		}else if(tipoComando == TipoComando.SINAL && !textoAtual.contains("-")){
			textoAtual = "-" + textoAtual;
		}else if(tipoComando == TipoComando.NUMERO 
				|| tipoComando == TipoComando.VIRGULA) {
			textoAtual = substituir ? texto : textoAtual + texto;
			substituir = false;
		}else {
			substituir = true;
			textoAtual = obterResultadoOperacao();
			textoBuffer = textoAtual;
			ultimaOperacao = tipoComando;
		}
		
			observadores.forEach(o -> o.valorAlterado(getTextoAtual()));
			
	}

	private String obterResultadoOperacao() {
		
		
		
		
		
		if(ultimaOperacao == null || 
				ultimaOperacao == TipoComando.IGUAL) {
			return textoAtual;
		}
		double numeroBuffer =
				Double.parseDouble(textoBuffer.replace(",", "."));
		double numeroAtual = 
				Double.parseDouble(textoAtual.replace(",", "."));
		double resultado = 0;
		
		if(ultimaOperacao == TipoComando.SOMA) {
			resultado = numeroBuffer + numeroAtual;
		}else if(ultimaOperacao == TipoComando.SUB) {
			resultado = numeroBuffer - numeroAtual;
		}else if(ultimaOperacao == TipoComando.MULT) {
			resultado = numeroBuffer * numeroAtual;
		}else if(ultimaOperacao == TipoComando.DIV) {
			resultado = numeroBuffer / numeroAtual;
		}
		String resultadoString = Double.toString(resultado).replace(".", ",");
		boolean inteiro = resultadoString.endsWith(",0");
		return inteiro ? resultadoString.replace(",0", "")
				: resultadoString;
	}

	
	private TipoComando detectarTipoComando(String texto) {
		// TODO Auto-generated method stub
		if(textoAtual.isEmpty() && texto == "0") {
			return null;
		}
		
		try {
			Integer.parseInt(texto);
			return TipoComando.NUMERO;
		} catch (NumberFormatException e) {
			// quando nao for numero..
			if("AC".equals(texto)) {
				return TipoComando.ZERAR;
			}else if("/".equals(texto)) {
				return TipoComando.DIV;
			}else if("*".equals(texto)) {
				return TipoComando.MULT;
			}else if("+".equals(texto)) {
				return TipoComando.SOMA;
			}else if("-".equals(texto)) {
				return TipoComando.SUB;
			}else if("±".equals(texto)) {
				return TipoComando.SINAL;	
			}else if("=".equals(texto)) {
				return TipoComando.IGUAL;
			}else if(",".equals(texto) && !textoAtual.contains(",")) {
				return TipoComando.VIRGULA;
			}
		}
		
		return null;
	}
}
	
	
	
	
	
	
	
	

	


