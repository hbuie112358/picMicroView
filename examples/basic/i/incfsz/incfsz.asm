;;;;;;; incfsz.asm ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;
;This program tests the incfz instruction. Incfsz increments the contents of 
;register f. If the result equals 0, the next instruction is skipped.
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
	
		movlw	0xff		;Place 0xff in wreg
		movwf	CNT		;Copy wreg value to CNT
HERE		incfsz	CNT, 1		;Increment contents of f, skip next instruction if zero
		goto 	stop
		goto 	CONTINUE
CONTINUE	movlw	0x00		;Place 0 in wreg
		movwf	CNT		;Copy wreg value into CNT
		goto	HERE
	

stop		goto		stop

	end

