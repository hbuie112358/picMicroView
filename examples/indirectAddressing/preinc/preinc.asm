;;;;;;; preinc.asm ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;
;This program tests indirect addressing mechanism writes/reads to PREINCx
;
;
;;;;;;; Program hierarchy ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;
; Mainline
;  
;
;;;;;;; Assembler directives ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

	#include p18f452.inc
        list  P=PIC18F452, F=INHX32, C=160, N=0, ST=OFF, MM=OFF, R=DEC, X=ON

;;;;;;; Variables ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

	cblock  0x000           ;Beginning of Access RAM

	endc

;;;;;;; Macro definitions ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;;;;;;; Vectors ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;


        org  0x0000             ;Reset vector
        nop
        goto  Mainline

        org  0x0008             ;High priority interrupt vector
        goto  $                 ;Trap

        org  0x0018             ;Low priority interrupt vector
        goto  $                 ;Trap

;;;;;;; Mainline program ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

Mainline
;This section tests indirect addressing preinc operation. It uses preinc to create a 
;table of values in access memory from 0xffe to 0x001. Checks rollover from 0xfff to 
;0x000. It writes values, resets the FSR, then reads the values back into the wreg.
;Write test:

	movlw	0xaa		;Place 0xaa in wreg
	lfsr	0, 0xffd	;Place 0xffd in FSR0
	movwf	PREINC0		;Increment FSR0, write wreg to address pointed to by FSR0 
	movlw	0x0e		;Place 0x0e in wreg
	movwf	PREINC0		;Increment FSR0, write wreg to address pointed to by FSR0 
	movlw	0x02		;Place 0x02 in wreg
	movwf	PREINC0		;Increment FSR0, write wreg to address pointed to by FSR0 
	movlw	0x1c		;Place 0x1c in wreg
	movwf	PREINC0		;Increment FSR0, write wreg to address pointed to by FSR0 

;Read test:

	lfsr	0, 0xffd	;Place 0xffd in FSR0
	movff	PREINC0, WREG
	movff	PREINC0, WREG
	movff	PREINC0, WREG
	movff	PREINC0, WREG


stop	goto		stop

	end





















