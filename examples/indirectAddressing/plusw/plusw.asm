;;;;;;; plusw.asm ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;
;This program tests indirect addressing mechanism writes/reads to PLUSWx
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
	temp1
	temp2
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
;This section tests indirect addressing plusw operation. It uses plusw to assign values
;to memory locations in access memory by placing a signed value in wreg. Plusw then adds
;this signed wreg value as an offset to the FSR and places a value from a register into 
;the memory location pointed to by fsr + wreg.

;Write test:
;Check writing with rollunder from 0x000 to 0xfff

	lfsr	0, 0x002	;Place 0x002 in FSR0
	movlw	0x06		;Place 0x06 in wreg
	movwf	PLUSW0		;Write wreg to address pointed to by FSR0 + wreg
	movlw	0xfa		;Place 0xfa (-6) in wreg
	movwf	PLUSW0		;Write wreg to address pointed to by FSR0 + wreg

;Read test:
;Check reading with rollunder from 0x000 to 0xfff

	movlw	0x06		;Place 0x06 in wreg
	movff	PLUSW0, temp1	;Put value in memory location pointed to by FSR0+wreg into temp1
	movlw	0xfa		;Place 0xfa (-6) in wreg
	movff	PLUSW0, temp2	;Put value in memory location pointed to by FSR0+wreg into temp2

;Write test:
;Check writing with rollover from 0xfff to 0x000

	lfsr	1, 0xffe	;Place 0xffe in FSR1
	movlw	0x0e		;Place 0x0e in wreg
	movff	temp1, PLUSW1	;Write temp1 into memory location pointed to by FSR1+wreg (0x009)
	movlw	0xf2		;Place 0xf2 (-14) in wreg
	movff	temp2, PLUSW1	;Write temp1 into memory location pointed to by FSR1+wreg (0xff3)



stop	goto		stop

	end





















