;;;;;;; mullw.asm ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;
;This program tests operation of mullw instruction. Mullw carries out 
;unsigned multiplication between literal k and contents of wreg. 16 bit
;result is placed in PRODH:PRODL register pair. Wreg is unchanged, no 
;status bits affected.
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

FACTOR 	equ	0xc4
start	movlw		0xe2		;Place 0xe2 in wreg
	mullw		0xc4		;Multiply 0xe2, 0xc4, result in PRODH:PRODL (product is 0xad08)
	mullw		0x00		;Multiply wreg by 0 (to clear PRODH:PRODL)
	movlw		0xe2		;Place 0xc4 in wreg
	mullw		FACTOR		;Multiply wreg, FACTOR, result in PRODH:PRODL (0xad08)
	

stop	goto		stop

	end






















