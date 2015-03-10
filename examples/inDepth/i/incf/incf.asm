;;;;;;; incf.asm ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;
;This program tests the operation of the incf instruction. Incf increments
;the contents of register f.
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

;Example 1:

	movlb	0x01		;Set banked addressing to use bank 1
	movlw	0xff		;Place 0xff in wreg
	movwf	CNT, 1		;Copy contents of wreg to CNT
	incf	CNT, 1, 1	;Using banked addressing, increment register CNT

;Example 2:

	lfsr	0, 0x0c2	;Place 0x0c2 in FSR0
	movlw	0xff		;Place 0xff in wreg
	movwf	INDF0, 1	;Copy wreg to register pointed to by FSR0
	incf	INDF0, 1, 1	;Increment register pointed to by FSR0

;Example 3:

	movlw	0x10		;Place 0x10 in wreg
	movwf	CNT, 1		;Copy wreg to CNT
	incf	CNT, 0, 1	;Increment contents of CNT, place result in wreg,
				;value in CNT does not change
		

stop		goto		stop

		end
