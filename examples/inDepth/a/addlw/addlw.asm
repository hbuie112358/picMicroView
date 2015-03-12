;;;;;;; addlw.asm ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;
;This program tests operation of addlw instruction according to the examples in
;the pic18 user manual. Addlw adds a literal k to the contents of the wreg and 
;places result in wreg.
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

	cblock  0x037           ;Beginning of Access RAM
	MYREG
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

	movlw	5	;Place 5 in wreg
	addlw	10	;Add 10, wreg, result in wreg (sum is 0x0f)

;Example 1:
	movlw	0x18	;Place 0x18 in wreg
	addlw	0x19	;Add 0x19 to contents in wreg (DC bit 1 in status
				;register is set since carry from bit 3 to bit 4)

;Example 2:
	movlw	0x60	;Place 0x60 in wreg
	addlw	MYREG	;Add address of MYREG (0x037) to contents in wreg (OV bit 3 
			;in status register is set since carry from bit 6 to bit 7)

;Example 3: (to be implemented)

;Example 4:

	movlw	0x02	;Place 0x02 in wreg
	addlw	PCL	;Add value in PCL to wreg (*note* - example in user manual
			;says that PCL is at 0xff8 but that's a mistake. Other places
			;in same manual clearly states PCL at 0xff9, so result is 0xfb
			;not 0xfa as stated in example in manual)

;Example 5:

Offset	equ	0x02
	movlw	0x10	;Place 0x10 in wreg
	addlw	Offset	;Add value of constant Offset to wreg
	

stop	goto	stop

	end

