package Proyecto;

public class If extends Statement{

	Tokens i;
	Expresion expr;
	Statement es;
	String 	Clasificacion;
	
	
	If(Tokens IF, Expresion e, Statement state, String Clasif){
		super(state.id,state.operador,state.expr,state.Clasificacion);
		this.i=IF;
		this.expr=e;
		this.Clasificacion=Clasif;
		
	}
	
	//Constructor
	If(Tokens I, Expresion e, String Clasif)
	{
		i=I;
		expr=e;
		Clasificacion=Clasif;
	}
	
	
	//Modificacion
	public String getClasificacion(){
		return Clasificacion;
	}
	

	public String getIF() {
		return i.getToken();
	}

	public Expresion getExpresion() {
		return expr;
	}

	public String toString(){
		return i.getToken()+"("+expr.toString()+")";
	}
	
	
	
}
