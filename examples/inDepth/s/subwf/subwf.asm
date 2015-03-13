;;;;;;; subwf.asm ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;
;This program tests operation of subwf instruction. Subwf subtracts wreg from
;register f (two's complement method). This instruction is tested 
;according to the examples in the pic18 user manual.
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
	REG1
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

;Example 1:
;Case 1:

	movlb	0x01		;Select bank 1 of data memory
	movlw	0x03		;Place 0x03 in wreg
	movwf	REG1, 1		;Copy wreg to REG1
	movlw	0x02		;Place 0x02 in wreg
	subwf	REG1, 1, 1	;Subtract wreg from REG1 (at 0x100), result in f
				;REG1: 0x01, status register C, DC are set
;Case 2:

	movlw	0x02		;Place 0x02 in wreg
	movwf	REG1, 1		;Copy wreg to REG1
	movlw	0x02		;Place 0x02 in wreg
	subwf	REG1, 1, 1	;Subtract wreg from REG1 (at 0x100), result in f
				;REG1: 0x00, status register C, DC, Z bits are set

;Case 3:

	movlw	0x01		;Place 0x01 in wreg
	movwf	REG1, 1		;Copy wreg to REG1
	movlw	0x02		;Place 0x02 in wreg
	subwf	REG1, 1, 1	;Subtract wreg from REG1 (at 0x100), result in f
				;REG1: 0xff, status register N bit is set
	
stop	goto		stop

	end

