package Proyecto;

import java.util.Vector;


public class ParserClass {
        
    
	int index;
	Vector <Tokens> tokens;
	Vector <Error> errors;
	
	Vector <VarDeclaration> VarDec; //Guadar declaraciones de variables
        
	Vector <Statement> States; //Guardar Statements
	
	//Variables declaradas
	
	Vector <String> ID;
	Vector <String> Tipo;
	
	static Tokens variable; 
	static String Estring;
	
	static int Cont=0;
	static int Jumps=0;
        
	static int saltos=0;
	
	static int inutil;
	static int basura;
	
	
	int contError;
	int lines;
	Tokens currentToken;
       
  	String res="";
  	String ressem="";
  	
  	boolean Correcto=true;
  	
  	//Constructor del parser. Inicializ variables  y llama al m�todo que hace el trabajo
 	public ParserClass(Vector <Tokens> tokens){
 		this.tokens=tokens;
 		index=0;//inicia en el token de la posici�n 0
 		nextToken();
 		variable = new Tokens("","INVALIDO",-1);
 		errors= new Vector <Error>();
 		VarDec= new Vector <VarDeclaration>();
              
 		States= new Vector <Statement>();
 	
 		Tipo = new Vector <String>();
 		ID = new Vector <String>();
 		
 		lines=0;
 		Program();
 	}
 	
 	//GRAMATICA
 	/*
 	Program -> VarDeclaration* Statement* <EOF>
 	VarDeclaration -> ("int | float") Identifier ";" 
 	Statement -> Identifier "=" Expresion";"
 	Statement -> "if" "(" Expresion ")" Statement 
 	Statement -> "else (este no funca bien)
 	Expresion -> Identifier ("=="|"!=") Identifier | Identifier 
 	Identifier -> letra (letra | digito )*
 	*/
 	
 	public void Program(){
 		
 		//IDENTIFICADOR, IF Y else
 		if(currentToken.getCode()==21 || currentToken.getCode()==0 || currentToken.getCode()==1){}
 		else{
 			VarDeclaration();
 		}
 		
 		while(currentToken.getCode()==21||currentToken.getCode()==0||currentToken.getCode()==1)
 		{
 			if(Statements()){
 				nextToken();
 			}
 			else
 				errors.addElement(new Error(errors.size()+1,"Codigo Invalido","Statement mal declarado"));
 		}
 		
 		//Hay codigo basura
 		if(currentToken.getCode()==2|| currentToken.getCode()==3||currentToken.getCode()==12 
 				|| currentToken.getCode()==10 || currentToken.getCode()==11 || currentToken.getCode()==26
 				|| currentToken.getCode()==19 || currentToken.getCode()==9 || currentToken.getCode()==11 )
 		{
 			System.out.println("");
 			errors.addElement(new Error(errors.size()+1,"Codigo Invalido","Hay codigo basura"));
 			Correcto=false;
 		}
 		
 		System.out.println("");
 		System.out.println("ARBOL DE DECLARACION DE VARIABLES");
 		System.out.println("");
            
 		
                
                
 		
                if(VarDec.size() == 0){
                    for(int i=0;i<VarDec.size();i++){
                        VarDec.add(new VarDeclaration(tipoDato(),ID()));    
                        System.out.println(VarDec.elementAt(0).getType()+"    "+VarDec.elementAt(0).getId());                    
 		 }
                }/*if(VarDec.size()>=1){
                    VarDec.add(new VarDeclaration(tipoDato(),ID()));
                    for(int i=1;i<VarDec.size();i++){
                        if(VarDec.elementAt(i).equals(VarDec.elementAt(i-1))){
                            errors.addElement(new Error(errors.size()+1,"Codigo Invalido","Variable ya declarada"));
                        }else{
                            System.out.println(VarDec.elementAt(i).getType()+"    "+VarDec.elementAt(i).getId());   
                        }
 		 }
                }*/
                if(VarDec.size()>=1){
                    VarDec.add(new VarDeclaration(tipoDato(),ID()));
                    for(int i=1;i<VarDec.size();i++){
                        if(VarDec.elementAt(i).getId() == VarDec.elementAt(i-1).getId()){
                            errors.addElement(new Error(errors.size()+1,"Codigo Invalido","Variable ya declarada"));
                        }else   
                            //System.out.println(VarDec.elementAt(i).getType()+"    "+VarDec.elementAt(i).getId());   
                            System.out.println(VarDec.elementAt(i-1).getId());
                        }
                    }
                
                
                    
                   
             
                
                
                
                
 	
 		System.out.println("");
 		System.out.println("ARBOL DE ESTATUTOS");
 		System.out.println("");
 		
 		//SIEMPRE EL ULTIMO STATEMENT QUE AGREGO AL VECTOR SE IMPRIME PRIMERO, PERO TODOS LOS DEMAS SI SE IMPRIMEN EN EL ORDEN
 		//GOTTA CHECK THIS
 		if(errors.size()==0){
 			
 			/*for(int i=States.size()-1; i>=0; i--){
	 		    System.out.println(States.elementAt(i).toString());
	 		}*/
	 		
 			System.out.println("");
	 		for(int i=0; i<States.size(); i++){
	 		    System.out.println(States.elementAt(i).toString());
	 		 }
	 	
 		}
 		else
 			System.out.println("El programa tiene errores,asi que no habra arbol sintactico");
 		
 		System.out.println("");
 		printRes();
 		System.out.println(res);
 		
 		
 	
 		
 		
 	}
 	

	//ANALIZADOR SINTACTICO
	
 	public boolean VarDeclaration()
 	{
 		boolean correcto = false;
                boolean error = false;
		while(tipo_dato())	
 		{
					VarDec.add(new VarDeclaration(tipoDato(),ID()));
					
					if(VarDec.lastElement().getType().equals("")||VarDec.lastElement().getId().equals(""))
                                        {
						VarDec.remove(VarDec.lastElement());
                                        }
                                        /*if(!validarNombre(VarDec,VarDec.lastElement())){
                                            if(errors.size()==0)
						{
							errors.addElement(new Error(errors.size()+1,"Error","Variable Duplicada"));
						}
                                           
                                        }*/
					//pregunta por ";"
					if(currentToken.getCode()==12)
					{
						Correcto=true;
						nextToken();
					}
					else
					{
					
						Correcto=false;
						
						if(errors.size()==0)
						{
							errors.addElement(new Error(errors.size()+1,"Signo especial","Falta punto y coma"));
						}
						
						return false;
					}	
			}
 		
 		return correcto;
 	}
        
        
 	public boolean Statements()
 	{
 		boolean correcto = false;
 		int caracter;
 		String Clasificacion;
 		caracter=currentToken.getCode();
 
 		Statement State= new Statement();
 		
 		switch(caracter)
 		{
 			//Caso identificadores 
 			//indetifier=expresion
 			case 21:
 			{
 				Tokens iden= currentToken;
 				nextToken();
 				//si el token actual es un =
 				if(currentToken.getCode()==9)
 				{
 					//Checar la expresion
 					Tokens op = currentToken;
 					nextToken();
 					Expresion expr= Expresion();
 					
 					if(expr.isExpresion()){
 						if(currentToken.getCode()==12) //Preguntar por punto y coma
 						{	 
							//nextToken();
 							//Statement tipo
 							//-----------------------------identifier = (Expresion)
 							Clasificacion="A";
 							State=new Statement(iden,op,expr,Clasificacion);
 							States.addElement(State);
 							return correcto =true;
 						}	
 						else
 						{
 							errors.addElement(new Error(errors.size()+1,"","Falta Punto y Coma"));
 							return correcto=false;
 						}
 					}
 					else{
 	 					errors.addElement(new Error(errors.size()+1,"Statement 1","ASIGNACI�N mal declara 2"));
 	 	 				return false;
 					}
 				}
 				else
 				{
 					errors.addElement(new Error(errors.size()+1,"Statement 1","ASIGNACI�N mal declara 1"));
 					return  false;
 				}
 				
 			}
 			//caso if
 			case 0:
 			{
 				Tokens condicion=currentToken;
 				nextToken();
 					//pregunta si sigue un (
 					if(currentToken.getCode()==10)
 					{
 						nextToken(); //Consumir el (
 						Expresion expr = Expresion(); //checar expresion						
 						
 						if(expr.isExpresion())
 						{
 							nextToken();
 							
 							//Tipo de statement
 							//if (Expresion)
 							Clasificacion="B";
 							States.add(new If(condicion,expr,Clasificacion));
 							
 							if(Statements())
 							{	
 								correcto=true;
 	 							return correcto;
 							}
 	 						else{
 	 							States.remove(States.lastElement());
 	 							System.out.println(currentToken.getCode());  
 								errors.addElement(new Error(errors.size()+1,"Statement 2","IF al declarado Caso 3"));
 								return correcto;
 							}
 			
 						}
 						else
 						{
 							System.out.println(currentToken.getCode());
 							errors.addElement(new Error(errors.size()+1,"Statement 2","IF al declarado Caso 2"));
 							return false;
 						}	
 					}
 					else
 					{
 						errors.addElement(new Error(errors.size()+1,"Statement 2","IF mal declarado Caso 1"));
 						return false;
 					}
 			}
 			//caso else
 			case 1:
 			{
 				
 				nextToken();
                                
                                        //pregunta por un print
                                        if(currentToken.getCode()==4){
                                            Tokens iden= currentToken;
                                            Tokens op = currentToken;
 					nextToken();
 					Expresion expr= Expresion();
 					
 					if(expr.isExpresion()){
 						if(currentToken.getCode()==12) //Preguntar por punto y coma
 						{	 
							//nextToken();
 							//Statement tipo
 							//identifier = (Expresion)
 							Clasificacion="A";
 							State=new Statement(iden,op,expr,Clasificacion);
 							States.addElement(State);
 							return correcto =true;
 						}	
 						else
 						{
 							errors.addElement(new Error(errors.size()+1,"","Falta Punto y Coma"));
 							return correcto=false;
 						}
 					}
 					else{
 	 					errors.addElement(new Error(errors.size()+1,"Statement 1","ASIGNACI�N mal declara 2"));
 	 	 				return false;
 					}
                                             
                                        }
 					//pregunta si sigue un id/num
                                            
                                            
                                        else if(currentToken.getCode()==21)
 					{
 						Tokens iden= currentToken;
 				nextToken();
 				//si el token actual es un =
 				
                                
                                
                                if(currentToken.getCode()==9)
 				{
 					//Checar la expresion
 					Tokens op = currentToken;
 					nextToken();
 					Expresion expr= Expresion();
 					
 					if(expr.isExpresion()){
 						if(currentToken.getCode()==12) //Preguntar por punto y coma
 						{	 
							//nextToken();
 							//Statement tipo
 							//identifier = (Expresion)
 							Clasificacion="A";
 							State=new Statement(iden,op,expr,Clasificacion);
 							States.addElement(State);
 							return correcto =true;
 						}	
 						else
 						{
 							errors.addElement(new Error(errors.size()+1,"","Falta Punto y Coma"));
 							return correcto=false;
 						}
 					}
 					else{
 	 					errors.addElement(new Error(errors.size()+1,"Statement 1","ASIGNACI�N mal declara 2"));
 	 	 				return false;
 					}
 				}
 				else
 				{
 					errors.addElement(new Error(errors.size()+1,"Statement 1","ASIGNACI�N mal declara 1"));
 					return  false;
 				}
 				
 			}
                                        
                                        
                                        
 					else
 					{
 						errors.addElement(new Error(errors.size()+1,"Statement 3","else mal declarado Caso 1"));
 						return false;
 					}
 			}
 			default:
 				return correcto;
 		} 				
 
 	}

 	public Expresion Expresion() {
 		
 		//El caso base de expresion es un token vacio 
 		Expresion Expr=new Expresion(new Tokens("",""),"");
 		
 		String clasificacion;
 		
 		//Pregunta por identificador
 		if(currentToken.getCode()==21){
 			
 			Tokens id=currentToken;
 			nextToken();
 			//pregunta por el == o un !=
 			if(currentToken.getCode()==19||currentToken.getCode()==26){
 				
 				Tokens op = currentToken;
 				nextToken();
 				//pregunta por identificador 
 				if(currentToken.getCode()==21)
 				{
 					
 					//Expresion tipo:
 					//identifier =  Identifier (==|!=) Identifier
 					clasificacion="a";
 					Expr=new Expresion(id,op,currentToken,clasificacion); //Expresion tipo id(!=|==)id que no funciona el !=
                                        
 					nextToken();
 				}
 				else{
 					errors.addElement(new Error(errors.size()+1,"Expresion "+lines," Se espera un id"));
 				}
	 	    }
	 	    else{
	 	    	
	 	    	//Tipo de expresion:
				//identifier
	 	    	clasificacion="b";
	 	     	Expr=new Expresion(id,clasificacion);
	 	     	System.out.println(Expr.toString());
	 	    }
	   }
 		else{
	 	    errors.addElement(new Error(errors.size()+1,"Expresion "+lines," Se espera un id"));
	  }
	  return Expr;
 	}
 	
 	//Actualiza l variable "currentToken" al siguiente token en el vector
 	public void nextToken(){
 		if(index<tokens.size()){//si el indice es menor que el tamaño del vector
 				currentToken=tokens.elementAt(index);
 				index++;
 		}
 		else{
 			currentToken= new Tokens("INVALIDO","INVALIDO",-1);
 		}
 	}
 	 	
 	public Tokens tipoDato(){
 		Tokens td=variable; //variable = new Tokens("INVALIDO","INVALIDO",-1);
 		if (tipo_dato())	
 		{
 			td=currentToken;
 			nextToken();
 		}
 		else
 			errors.addElement(new Error(errors.size()+1,"Expresion "+lines, "Se espera un tipo de dato"));
                
 		return td;
 	}	
 
 	public Tokens ID(){
 		Tokens id=variable;
 		if(currentToken.getCode()==21){
 			id=currentToken;
 			nextToken();
 		}
 		else
 			errors.addElement(new Error(errors.size()+1,"Declaracion de Variables "+lines, "Se espera un id"));				
 		return id;
 	}
 	
 	public boolean tipo_dato(){
   		if(currentToken.getCode()==2||currentToken.getCode()==3)
    		return true;
   		
   		return false;
  	}
        
        public boolean validarNombre(Vector <VarDeclaration> V, VarDeclaration n){
            boolean repetido = false;
            System.out.println("Ciclo");
            if(V.size()<= 1){
                return false;
            }else{
                for(int i = 1; i<V.size();i++){
                    if(V.elementAt(i).getId().toString() == V.elementAt(i-1).getId().toString())
                    {
                        return repetido = true;
                    }else{
                        return repetido = false;
                    }
                }
            }
            return repetido;
        }
 	
    public void printRes(){
    	if(errors.size()==0){
    		res="El programa es correcto ";
    	}else{
    		Correcto = false;
    		res="Se encontraron "+errors.size()+" errores en el programa"+"\n";
    		for(int i=0;i<errors.size();i++){
    			res+="Error "+errors.elementAt(i).getNum()+": "+errors.elementAt(i).getLocation()+","+errors.elementAt(i).getDesc()+"\n";
    		}
    	}
    }
    
    public boolean isCorrect(){
    	return Correcto;
    }
    
    public String Resultado(){
    	return res;
    }
    
    public String ResultadoSemantico()
    {
    	return ressem;
    }
    
}

class Error{
	int num;
	String location;
	String desc;
	
	public Error(int num, String location, String desc){
		this.num=num;
		this.location=location;
		this.desc=desc;
	}
	
	public int getNum(){
		return num;
	}
	
	public String getLocation(){
		return location;
	}
	
	public String getDesc(){
		return desc;
	}
} 