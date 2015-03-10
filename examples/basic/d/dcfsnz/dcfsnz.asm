;;;;;;; dcfsnz.asm ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;
;This program tests the dcfsnz instruction. Dcfsnz decrements the contents of 
;register f. If the result is not 0, the next instruction is skipped.
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
	CNT
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
	
		movlb	0x01		;Set banked addressing for bank 1
		movlw	0x01		;Place 1 in wreg
		movwf	CNT		;Copy wreg value to CNT
HERE		dcfsnz	CNT, 1		;Decrement contents of f, skip next instruction if not zero
		goto 	CONTINUE
		goto 	stop
CONTINUE	movlw	0x02		;Place 2 in wreg
		movwf	CNT		;Copy wreg value into CNT
		goto	HERE
	

stop		goto		stop

	end





















