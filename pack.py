import os

#----CONFIG----
PACKER_PATCH = "J:\\Instalki\\packer\\packer.jar"
SCALER_PATCH = "J:\\Programy\\IM\\convert.exe"
DUPA = "J:\\dupa.txt"
#--------------

def scale(input, output, percent):
	dir = os.listdir(input)
	i = 0.0
	print("----Scalling----");
	for d in dir:
		print("    Scaling img from folder "+d+":")
		for img in os.listdir(output+d):
			if img[-3:] == 'png':
				os.system('del '+output+d+'\\'+del_d)
				s = SCALER_PATCH+" -resize \""+percent+"%"+"\" \""+input+d+"\\"+img+"\" \""+output+d+"\\"+img+"\""
				os.system(s)
	print("----Finished----")
	
def pack(input, output):
	print("----Packing----")
	os.system('del /s /q "'+output+'*" > '+DUPA)
	os.system('del '+DUPA)
	for d in os.listdir(input):
		os.system(PACKER_PATCH+' --silent --project="'+input+d+"\\"+d+'.prj"')
		os.system('rename "'+output+d+'\\pack" "'+d+'"')

pack("J:\\Ludum Dare\\22-08-2012\\art\\", "J:\\Ludum Dare\\22-08-2012\\ludumdare-android\\assets\\textures\\");