;;;;;;;addwfc.asm ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;
;This program tests operation of the addwfc instruction. It adds the 
;contents of the WREG and the carry bit to the contents of register f.
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
	temp
	endc

;Define variable temp1 at ram address 0x100
	cblock  0x100           ;Beginning of bank 1 RAM
	tempBank1
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
;Test no carry, use bsr, write wreg:

	movlw	0x17
	movlb	1
	movwf	tempBank1	;Store 0x17 at address 0x100
	movlw	0xc2
	addwfc	tempBank1, 0, 1	;Add WREG, C bit, and temp, 
				;place result (0xd9) in WREG (no carry)

;Test no carry, use bsr, write freg:
	movlw	0x18
	movlb	1
	movwf	tempBank1	;Store 0x18 at address 0x100
	movlw	0xc2
	addwfc	tempBank1, 1, 1	;Add WREG, C bit, and temp, 
				;place result (0xd9) in tempBank1 (no carry)

;Test no carry, no bsr, write wreg:

	movlw	0x17
	movlb	1
	movwf	temp		;Store 0x17 at address 0x100
	movlw	0xc2
	addwfc	temp, 0		;Add WREG, C bit, and temp, 
				;place result (0xd9) in WREG (no carry)

;Test no carry, no bsr, write freg:
	movlw	0x18
	movlb	1
	movwf	temp		;Store 0x18 at address 0x000
	movlw	0xc2
	addwfc	temp, 1		;Add WREG, C bit, and temp, 
				;place result (0xd9) in temp (no carry)

;Cause carry bit to be set:

	movlw	0xfe
	movwf	temp
	movlw	0xad
	addwfc	temp, 0		;Set carry bit

;Test add with carry:

	movwf	temp		
	movlw	0x02
	addwfc	temp, 0		;Add WREG, C bit, and temp (0x02 + 1 + 0xab), 
				;place result (0xae) in WREG (clears carry bit)

stop	goto		stop

	end





















