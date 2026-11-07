## Definicion del proyecto
Es una aplicacion sencilla, que muestre los datos necesarios para hacer la declaracion
mensual para pagos definitvos en el ezquema plataformas tecnologicas, su intencion es
agilizar el calculo.

Necesiata dos valores de entrada, la ganancia total(4 semanas), y el xml, para obtener
el isr retenido por la plataforma, la ganancia por intermedierio.

Con los datos obtendios, se calcula el dinero en efectivo.

Toda la informacion se guarda, para luego mostrala en otra pantalla, y asi llevar un 
registro de los meses.

## Especificaciones tecnicas
- Echo en android studio
- Sistema para lectura de xml(incorporado en android)
    - Puede leer 3 o mas xml, al mismo tiempo
    - Al introducir los datos deseados, se genera el calculo de isr e iva
    - Se visualizan todas las declaraciones en un activity diferente

## Inventario de contenido
- Pantalla para mostrar dos opciones: calcular impuesto y mostrar mensualidades registradas
- Pantalla para calcular impuesto: semanas y xml
- Pantalla mostrar mensualidades: dienero efectivo, isr retenido, intermediario, y lo que se le va a pagar al sat
