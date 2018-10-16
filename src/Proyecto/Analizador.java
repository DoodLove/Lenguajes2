package Proyecto;
import java.util.Scanner;
import java.util.Vector;
 
public class Analizador {
	
	Scanner leer = new Scanner(System.in);
	//Arreglo de palabras reservadas
	String PR [] = new String []{"if","else","int","float","print","String","double","char","boolean","long","byte"};
	
	//Vector con los simbolos validos
 	Vector <Character> caracteresValidos = new Vector <Character> ();
	
	//Vector tipo tokens que guarda los tokens en un objeto
	 Vector <Tokens> tabla = new Vector <Tokens> (20,1);
	
	int ap;
	String linea;
	char caracter;
	String token;
	
	int renglon=1;
	int columna=0;
	
	//Variables para saber si hay errores
	boolean Correcto = true;
	
	//Construtor
	public Analizador(String Programa){
		
		llenaVector();
		ap = 0;
	//	ContError = 0;
		token = "";
		linea = Programa;
		IdentificaToken();
	}
	
	//Tokens validos
	public void llenaVector(){
		caracteresValidos.add('=');
		caracteresValidos.add('(');
		caracteresValidos.add(')');
		caracteresValidos.add(';');
		caracteresValidos.add(' ');
                
		
		
               
                
	}
	
	public void IdentificaToken(){
		
		//Analiza el primer caracter
		caracter = linea.charAt(ap);
		//Ciclo que termina al leerlo todo
		do{
			//Que tipo de caracter es
			switch(caracter){
			
			case '=':
			token+=caracter;
				SiguienteCaracter();
				
				if(caracter == '=')
				{
					token+=caracter;
					Interfaz.TokenTipo = Interfaz.TokenTipo + token+"\tOPREL,==\n"; 
					tabla.addElement(new Tokens(token, "OPREL")); //AGREGA A UN VECTOR DE OBJETOS TIPO TOKEN
					SiguienteCaracter();
				} 
				else if(caracter == '(' || caracter == ')' || caracter == ';' || Character.isDigit(caracter)){ 
					token+=caracter;
					Interfaz.TokenTipo = Interfaz.TokenTipo + token+"\tERROR"+"  R"+renglon+":C"+(columna-token.length())+"\n";
					Correcto = false;
					SiguienteCaracter();
				}
				else{
					Interfaz.TokenTipo = Interfaz.TokenTipo + token+"\tOPREL,=\n"; 
					tabla.addElement(new Tokens(token, "OPREL")); //AGREGA A UN VECTOR DE OBJETOS TIPO TOKEN
				}
				
				token = "";
				break;
				
				
            case ';':
                token+=caracter;
                Interfaz.TokenTipo = Interfaz.TokenTipo + token+"\tSigno de puntuación,;\n";
                tabla.addElement(new Tokens(token, "PuntoComa"));
                
                token ="";
                SiguienteCaracter();
                break;
                
			case ' ': //Espacio en blanco
				token+=caracter;
				SiguienteCaracter();
				while(caracter == ' ' && ap!=-1){
					SiguienteCaracter();
				}
				
				//System.out.println("\tEB");
				token = "";
				break;
				
			case 9: //Tabulador
				token+=caracter;
				columna+=3;
				SiguienteCaracter();
				while(caracter == 9 && ap!=-1){
					SiguienteCaracter();
					columna+=3;
				}
				
				//System.out.println("\tTABs");
				token = "";
				break;
				
			case '\n':
				token+=caracter;
				SiguienteCaracter();
				renglon++;
				columna=1;
				while(caracter == '\n' && ap!=-1){
					SiguienteCaracter();
					renglon++;
					columna=1;
				}
				
				//System.out.println("\tENTER");
				token = "";
				break;
				
			case '(':
                    token+=caracter;
                    Interfaz.TokenTipo = Interfaz.TokenTipo + token+"\tParentesis Izq\n";
                    tabla.addElement(new Tokens(token, "Parentesis izquierdo"));
                    token ="";
                    
                    SiguienteCaracter();
                    break;
                            
                            
                            
                         
                    
            case ')':
                    token+=caracter;
                    Interfaz.TokenTipo = Interfaz.TokenTipo + token+"\tParentesis Der\n";
                    tabla.addElement(new Tokens(token, "Parentesis derecho"));
                    token ="";
                    
                    SiguienteCaracter();
                    break;
                
			default:
				
			//	Identificadores 
				if(Character.isLetter(caracter))
				{
					token+=caracter;
					SiguienteCaracter();
				
					if(Character.isLetter(caracter) || Character.isDigit(caracter))
					{
						while(Character.isLetter(caracter)||Character.isDigit(caracter)){
							token+=caracter;
							SiguienteCaracter();
						}
						
					}

					if(!caracteresValidos.contains(caracter)){
						while(!caracteresValidos.contains(caracter)){
								token+=caracter;
								SiguienteCaracter();
						}
						if(columna-token.length()==0)
							Interfaz.TokenTipo = Interfaz.TokenTipo + token+"\tERROR"+"  R"+renglon+":C"+(columna-token.length())+"\n";
						else
							Interfaz.TokenTipo = Interfaz.TokenTipo + token+"\tERROR"+"  R"+renglon+":C"+(columna-1-token.length())+"\n";
						Correcto = false;
						token="";
						break;
					}
					
					if(PalabraReservada()){
						Interfaz.TokenTipo = Interfaz.TokenTipo + token+"\tPalabra Reservada \n";
						tabla.addElement(new Tokens(token, "PR"));
					}
					
					else{
						Interfaz.TokenTipo = Interfaz.TokenTipo + token+"\tIdentificador \n";
						tabla.addElement(new Tokens(token, "ID"));
					}
					token="";
				}
				else{
					
					while(!caracteresValidos.contains(caracter))
					{
						token+=caracter;
						SiguienteCaracter();
					}
					Interfaz.TokenTipo = Interfaz.TokenTipo + token+"\tERROR"+"  R"+renglon+":C"+(columna-token.length())+"\n";
					Correcto = false;

					token="";
					
					break;

				}
			}
		}while(ap>-1 && ap<linea.length());
		asignaCode();
		for(int i=0;i<tabla.size();i++){
			System.out.println(tabla.elementAt(i).token+"	"+tabla.elementAt(i).getCode());
		}
	}
	public Vector<Tokens> getVectorTokens(){
		return tabla;
	}
	
	public boolean isCorrect()
	{
		return Correcto;
	}
	
	public void asignaCode(){
		for(int i=0;i<tabla.size();i++){
			for(int a=0;a<PR.length;a++){
				if(tabla.elementAt(i).token.equals(PR[a])){//aquí cambié
					tabla.elementAt(i).setCode(a);//Es ppalabra reservada
				}
			}
			for(int b=0;b<caracteresValidos.size();b++){
				if(tabla.elementAt(i).getToken().equals(caracteresValidos.elementAt(b)+""))
					tabla.elementAt(i).setCode(b+9);//Es un simbolo de carcteres validos
			}
			if(tabla.elementAt(i).getToken().equals("=="))
				tabla.elementAt(i).setCode(19); //Es ==
                        if(tabla.elementAt(i).getToken().equals("!="))
				tabla.elementAt(i).setCode(26); //Es !=
                        
                        
                        
			
			if(tabla.elementAt(i).getTipo().equals("ID"))
					tabla.elementAt(i).setCode(21); //Es identificado		
		}
	}
	
	
	//Aumenta el apuntador para el sig caracter
	//Tambien aumenta a columna
	public void SiguienteCaracter(){
		ap++;
		columna++;
		if(ap>-1 && ap<linea.length())
			caracter = linea.charAt(ap);
		else{
			ap = -1;
			caracter = '\n';
		}
	}

	//Revisar por palabras reservadas
	public boolean PalabraReservada(){
		int cont = 0;
		do{
			if(token.equals(PR[cont])) //Las palabras reservadas deben estar escritas tal cual
				return true;
			
			cont++;
		}while(cont<PR.length);
		return false;
	}

}

//Clase de tokens que crea objetos con 2 atributos, token y tipo
class Tokens{
	
	String token;
	String tipo;
	int code;
	
	public Tokens(String t, String tp){
		token = t;
		tipo = tp;
	
	}
	public Tokens(String t, String tp, int n){
		token = t;
		tipo = tp;
		code=n;
	
	}
	
	public void setCode(int n){
		code=n;
	}
	public int getCode(){
		return code;
	}
	public String getToken(){
		return token;
	}
	public String getTipo(){
		return tipo;
	}
	public boolean esInicio(){
		boolean res=false;
		if (token=="INICIO")
			res=true;
		return res;
	}
	public boolean esFin(){
		boolean res=false;
		if (token=="FIN")
			res=true;
		return res;
	}
}
