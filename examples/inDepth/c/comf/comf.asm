;;;;;;; comf.asm ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;
;This program tests operation of comf instruction. Comf one's complements
;the contents of register f.
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

	movlb	1		;Select bank 1 of data memory
	movlw	0x13		;Place 0x13 in wreg
	movwf	REG1, 1		;Copy wreg value to REG1
	comf	REG1, 0, 1	;Complement value in REG1, result in wreg (N bit is set)

;Case 2:

	movlw	0xff		;Place 0xff in wreg
	movwf	REG1, 1		;Copy contents of wreg into REG1
	comf	REG1, 0, 1	;Complement value in REG1, result in wreg (Z bit is set)
	
;Case 3:
	
	movlw	0x00		;Place 0x00 in wreg
	movwf	REG1, 1		;Copy contents of wreg to REG1
	comf	REG1, 0, 1	;Complement value in REG1, result in wreg (N bit is set)


;Example 2:

	lfsr	0, 0xfc2	;Place 0xfc2 in FSR
	movlw	0xaa		;Place 0xaa in wreg
	movwf	INDF0, 1	;Copy wreg value into address pointed to by FSR0,
				;movwf ignores banked addressing portion of instruction	
	comf	INDF0, 1, 1	;Clear contents of register pointed to by FSR0, result in that register,
				;clrf again ignores banked addressing portion of instruction.

;Example 3:
	
	movlw	0xff		;Place 0xff in wreg
	movwf	REG1, 1		;Copy contents of wreg into REG1
	comf	REG1, 1, 1	;Complement value in REG1, result in REG1 (Z bit is set)
	
	

stop	goto		stop

	end

