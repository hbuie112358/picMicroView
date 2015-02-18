;;;;;;; postinc.asm ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;
;This program tests indirect addressing mechanism writes/reads to POSTINCx
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
;This section tests indirect addressing postinc operation. It uses postinc to create a 
;table of values in access memory from 0xffe to 0x001. Checks rollover from 0xfff to 
;0x000. It writes values, resets the FSR, then reads the values back into the wreg.
;Write test:

;	movlw	0x0f		;Place 0x0f in wreg
;	movwf	FSR0H		;Move 0x0f to FSR0H
;	movlw	0xfe		;Place 0xfe in wreg
;	movwf	FSR0L		;move 0x01 to FSR0L
	lfsr	0, 0xffe	;Place 0xffe in FSR0
	movlw	0xaa		;Place 0xaa in wreg
	movwf	POSTINC0	;Write wreg to address pointed to by FSR0, increment FSR0
	movlw	0x0e		;Place 0x0e in wreg
	movwf	POSTINC0	;Write wreg to address pointed to by FSR0, increment FSR0
	movlw	0x02		;Place 0x02 in wreg
	movwf	POSTINC0	;Write wreg to address pointed to by FSR0, increment FSR0
	movlw	0x1c		;Place 0x1c in wreg
	movwf	POSTINC0	;Write wreg to address pointed to by FSR0, increment FSR0

;Read test:

;	movlw	0x0f		;Place 0x0f in wreg
;	movwf	FSR0H		;Move 0x0f to FSR0H
;	movlw	0xfe		;Place 0xfe in wreg
;	movwf	FSR0L		;move 0x01 to FSR0L
	lfsr	0, 0xffe	;Place 0xffe in FSR0
	movff	POSTINC0, WREG
	movff	POSTINC0, WREG
	movff	POSTINC0, WREG
	movff	POSTINC0, WREG

	



stop	goto		stop

	end





















