;;;;;;; postdec.asm ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;
;This program tests indirect addressing mechanism writes/reads to POSTDECx
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
;This section tests indirect addressing postdec operation. It uses postdec to create a 
;table of values in access memory from 0x001 to 0xffe. Checks rollover from 0x000 to 
;0xfff. It writes values, resets the FSR, then reads the values back into the wreg.
;Write test:

;	movlw	0		;Place 0 in wreg
;	movwf	FSR0H		;Move 0 to FSR0H
;	movlw	0x01		;Place 0x01 in wreg
;	movwf	FSR0L		;move 0x01 to FSR0L
	lfsr	0, 0x001	;Place 0x001 in FSR0
	movlw	0xaa		;Place 0xaa in wreg
	movwf	POSTDEC0	;Write wreg to address pointed to by FSR0, decrement FSR0
	movlw	0x0e		;Place 0x0e in wreg
	movwf	POSTDEC0	;Write wreg to address pointed to by FSR0, decrement FSR0
	movlw	0x02		;Place 0x02 in wreg
	movwf	POSTDEC0	;Write wreg to address pointed to by FSR0, decrement FSR0
	movlw	0x1c		;Place 0x1c in wreg
	movwf	POSTDEC0	;Write wreg to address pointed to by FSR0, decrement FSR0

;Read test:

;	movlw	0		;Place 0 in wreg
;	movwf	FSR0H		;Move 0 to FSR0H
;	movlw	0x01		;Place 0x01 in wreg
;	movwf	FSR0L		;move 0x01 to FSR0L
	lfsr	0, 0x001
	movff	POSTDEC0, WREG
	movff	POSTDEC0, WREG
	movff	POSTDEC0, WREG
	movff	POSTDEC0, WREG

	



stop	goto		stop

	end





















