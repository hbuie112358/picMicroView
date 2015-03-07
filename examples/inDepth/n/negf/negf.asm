;;;;;;; negf.asm ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;
;This program tests the negf instruction. Negf negates the value in register
;f using two's complement. The result is placed in register f.
;
;
;;;;;;; Program hierarchy ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;
; Mainline
;  
;
;;;;;;; Assembler directives ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

        list  P=PIC18F452, F=INHX32, C=160, N=0, ST=OFF, MM=OFF, R=DEC, X=ON
        #include p18f452.inc
        __CONFIG  _CONFIG1H, _HS_OSC_1H  ;HS oscillator
        __CONFIG  _CONFIG2L, _PWRT_ON_2L & _BOR_ON_2L & _BORV_42_2L  ;Reset
        __CONFIG  _CONFIG2H, _WDT_OFF_2H  ;Watchdog timer disabled
        __CONFIG  _CONFIG3H, _CCP2MX_ON_3H  ;CCP2 to RC1 (rather than to RB3)
        __CONFIG  _CONFIG4L, _LVP_OFF_4L  ;RB5 enabled for I/O

;;;;;;; Variables ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

	cblock  0x000           ;Beginning of Access RAM
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

;Example 1:
;Case 1:
	
	movlb	1		;Select data memory bank 1
	movlw	0x3a		;Place 0x3a in wreg
	movwf	MYREG, 1	;Copy wreg value into MYREG
	negf	MYREG, 1	;Two's complement value in MYREG, result in MYREG,
				;N bit in status register is set

;Case 2:

	movlw	0xb0		;Place 0xb0 in wreg
	movwf	MYREG, 1	;Copy wreg value into MYREG
	negf	MYREG, 1	;Two's complement value in MYREG, result in MYREG,
				;N bit in status register is cleared, DC bit set

;Case 3:

	movlw	0x00		;Place 0x00 in wreg
	movwf	MYREG, 1	;Copy wreg value into MYREG
	negf	MYREG, 1	;Two's complement value in MYREG, result in MYREG,
				;N, C, DC bits in status register are set	

stop	goto	stop

	end













